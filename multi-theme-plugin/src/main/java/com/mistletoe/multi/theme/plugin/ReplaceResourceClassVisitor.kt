package com.mistletoe.multi.theme.plugin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * Created by mistletoe
 * on 2021/9/1
 **/
class ReplaceResourceClassVisitor(private val className: String?, classVisitor: ClassVisitor?) :
    ClassVisitor(Opcodes.ASM6, classVisitor) {

    companion object {
        const val SYSTEM_RESOURCE_CLASS = "androidx/core/content/ContextCompat"
        const val GET_DRAWABLE_METHOD_NAME = "getDrawable"
        const val GET_COLOR_METHOD_NAME = "getColor"
    }

    /**
     * 父类名
     */
    private var superName: String? = null

    /**
     * 该类实现的接口
     */
    private lateinit var interfaces: Array<String>

    /**
     * ASM进入到类的方法时进行回调
     *
     * @param access
     * @param name       方法名
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    override fun visitMethod(
        access: Int, name: String, desc: String, signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        var mv = cv.visitMethod(access, name, desc, signature, exceptions)
        if (className == SYSTEM_RESOURCE_CLASS) {
            println("meeting target  class:$SYSTEM_RESOURCE_CLASS")
            mv = object : AdviceAdapter(ASM6, mv, access, name, desc) {
                override fun visitCode() {
                    when (name) {
                        GET_DRAWABLE_METHOD_NAME -> {
                            println("meeting target  getDrawable function")
                            mv.visitFieldInsn(
                                GETSTATIC,
                                "com/magic/multi/theme/core/action/SkinLoadManager",
                                "Companion",
                                "Lcom/magic/multi/theme/core/action/SkinLoadManager\$Companion;"
                            )
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/multi/theme/core/action/SkinLoadManager\$Companion",
                                "getInstance",
                                "()Lcom/magic/multi/theme/core/action/SkinLoadManager;",
                                false
                            )
                            mv.visitVarInsn(ILOAD, 1)
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/multi/theme/core/action/SkinLoadManager",
                                "getDrawable",
                                "(I)Landroid/graphics/drawable/Drawable;",
                                false
                            )
                            mv.visitInsn(ARETURN)
                            mv.visitMaxs(2, 2)
                            mv.visitEnd()
                            println("meeting target  getDrawable function,replace finished")
                        }
                        GET_COLOR_METHOD_NAME -> {
                            println("meeting target  getColor function")
                            mv.visitFieldInsn(
                                GETSTATIC,
                                "com/magic/multi/theme/core/action/SkinLoadManager",
                                "Companion",
                                "Lcom/magic/multi/theme/core/action/SkinLoadManager\$Companion;"
                            )
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/multi/theme/core/action/SkinLoadManager\$Companion",
                                "getInstance",
                                "()Lcom/magic/multi/theme/core/action/SkinLoadManager;",
                                false
                            )
                            mv.visitVarInsn(ILOAD, 1)
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/multi/theme/core/action/SkinLoadManager",
                                "getColor",
                                "(I)I",
                                false
                            )
                            mv.visitInsn(IRETURN)
                            mv.visitMaxs(2, 2)
                            mv.visitEnd()
                            println("meeting target  getColor function,replace finished")
                        }
                        else -> {
                            super.visitCode()
                        }
                    }
                }

                override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
                    return super.visitAnnotation(desc, visible)
                }

                override fun visitFieldInsn(opcode: Int, owner: String, name: String, desc: String) {
                    super.visitFieldInsn(opcode, owner, name, desc)
                }
            }
        }
        return mv
    }

    /**
     * 当ASM进入类时回调
     *
     * @param version
     * @param access
     * @param name       类名
     * @param signature
     * @param superName  父类名
     * @param interfaces 实现的接口名
     */
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String,
        interfaces: Array<String>
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.superName = superName
        this.interfaces = interfaces
    }
}