package com.magic.theme.resource.proxy

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * Created by mistletoe
 * on 2021/9/1
 **/
class ReplaceResourceTransform : Transform() {
    companion object {
        val TARGET_CLAZZ_LIST = listOf(
            "androidx/core/content/ContextCompat.class",
            "androidx/appcompat/content/res/AppCompatResources.class"
        )
    }

    override fun getName(): String {
        return "MultiThemePlugin"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        val isIncremental = transformInvocation.isIncremental
        val outputProvider = transformInvocation.outputProvider

        if (!isIncremental) {
            outputProvider.deleteAll()
        }

        transformInvocation.inputs.forEach { input ->
            input.jarInputs.forEach { jarInput ->
                processJarInput(jarInput, outputProvider, isIncremental)
            }

            input.directoryInputs.forEach { directoryInput ->
                processDirectoryInput(directoryInput, outputProvider, isIncremental)
            }
        }
    }

    private fun processJarInput(
        jarInput: JarInput,
        outputProvider: TransformOutputProvider?,
        isIncremental: Boolean
    ) {
        if (isIncremental) {
            processJarInputIncremental(jarInput, outputProvider)
        } else {
            processJarInputNoIncremental(jarInput, outputProvider)
        }
    }

    private fun processJarInputNoIncremental(
        jarInput: JarInput,
        outputProvider: TransformOutputProvider?
    ) {
        transformJarInput(jarInput, outputProvider)
    }

    private fun processJarInputIncremental(
        jarInput: JarInput,
        outputProvider: TransformOutputProvider?
    ) {
        val dest = outputProvider?.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.DIRECTORY
        )
        when (jarInput.status) {
            Status.NOTCHANGED -> {
            }

            Status.ADDED -> {
                transformJarInput(jarInput, outputProvider)
            }
            Status.CHANGED -> {
                if (dest?.exists() == true) {
                    FileUtils.forceDelete(dest)
                }
                transformJarInput(jarInput, outputProvider)
            }
            Status.REMOVED ->
                if (dest?.exists() == true) {
                    FileUtils.forceDelete(dest)
                }
            else -> {
                // do nothing
            }
        }
    }

    private fun transformJarInput(jarInput: JarInput, outputProvider: TransformOutputProvider?) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            var jarName = jarInput.name
            val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length - 4)
            }
            val jarFile = JarFile(jarInput.file)
            val enumeration = jarFile.entries()
            val tmpFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            val jarOutputStream = JarOutputStream(FileOutputStream(tmpFile))
            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement() as JarEntry
                val entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                val inputStream = jarFile.getInputStream(jarEntry)
                if (TARGET_CLAZZ_LIST.contains(entryName)) {
                    println("======= meeting class :${entryName}")
                    jarOutputStream.putNextEntry(zipEntry)
                    val classReader = ClassReader(IOUtils.toByteArray(inputStream))
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val className = entryName.split(".class")[0]
                    val classVisitor = ReplaceResourceClassVisitor(className, classWriter)

                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    jarOutputStream.write(code)

                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            jarFile.close()
            val dest = outputProvider?.getContentLocation(
                jarName + md5Name,
                jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

    private fun processDirectoryInput(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider,
        isIncremental: Boolean
    ) {
        val dest = outputProvider.getContentLocation(
            directoryInput.file.absolutePath,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        if (isIncremental) {
            processDirectoryInputIncremental(directoryInput, dest)
        } else {
            processDirectoryInputNoIncremental(directoryInput, dest)
        }
    }

    private fun processDirectoryInputNoIncremental(directoryInput: DirectoryInput, dest: File) {
        transformDirectoryInput(directoryInput, dest)
    }

    private fun processDirectoryInputIncremental(directoryInput: DirectoryInput, dest: File) {
        FileUtils.forceMkdir(dest)
        val srcDirPath = directoryInput.file.absolutePath
        val destDirPath = dest.absolutePath
        val fileStatusMap = directoryInput.changedFiles
        fileStatusMap.forEach { entry ->
            val inputFile = entry.key
            val status = entry.value
            val destFilePath = inputFile.absolutePath.replace(srcDirPath, destDirPath)
            val destFile = File(destFilePath)

            when (status) {
                Status.NOTCHANGED -> {
                }

                Status.ADDED -> {
                    transformDirectoryInput(directoryInput, dest)
                }

                Status.CHANGED -> {
                    FileUtils.touch(destFile)
                    if (dest.exists()) {
                        FileUtils.forceDelete(dest)
                    }
                    transformDirectoryInput(directoryInput, dest)
                }

                Status.REMOVED ->
                    if (destFile.exists()) {
                        FileUtils.forceDelete(destFile)
                    }
                else -> {
                    // do nothing
                }
            }
        }
    }

    private fun transformDirectoryInput(directoryInput: DirectoryInput, dest: File) {
        if (directoryInput.file?.isDirectory == true) {
            val fileTreeWalk = directoryInput.file.walk()
            fileTreeWalk.forEach { file ->
                val name = file.name
                if (name.endsWith(".class") && !name.startsWith("R\$")
                    && "R.class" != name && "BuildConfig.class" != name
                ) {
                    val classReader = ClassReader(file.readBytes())
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val className = name.split(".class")[0]
                    val classVisitor = ReplaceResourceClassVisitor(className, classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    val fos =
                        FileOutputStream(file.parentFile.absoluteFile.toString() + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }
        FileUtils.copyDirectory(directoryInput.file, dest)
    }


}