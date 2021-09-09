package com.mistletoe.multi.theme.plugin

import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.*

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
     * ASM进入到类的方法时进行回调
     *
     * @param access
     * @param name
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
                            val label0 = Label()
                            mv.visitLabel(label0)
                            mv.visitLineNumber(455, label0)
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
                            mv.visitVarInsn(ALOAD, 0)
                            mv.visitVarInsn(ILOAD, 1)
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/multi/theme/core/action/SkinLoadManager",
                                "getDrawable",
                                "(Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;",
                                false
                            )
                            mv.visitInsn(ARETURN)
                            val label1 = Label()
                            mv.visitLabel(label1)
                            mv.visitLocalVariable(
                                "context",
                                "Landroid/content/Context;",
                                null,
                                label0,
                                label1,
                                0
                            )
                            mv.visitLocalVariable("id", "I", null, label0, label1, 1)
                            mv.visitMaxs(3, 2)
                            mv.visitEnd()
                            println("meeting target  getDrawable function,replace finished")
                        }
                        GET_COLOR_METHOD_NAME -> {
                            println("meeting target  getColor function")
                            val label0 = Label()
                            mv.visitLabel(label0)
                            mv.visitLineNumber(515, label0)
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
                            mv.visitVarInsn(ALOAD, 0)
                            mv.visitVarInsn(ILOAD, 1)
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/multi/theme/core/action/SkinLoadManager",
                                "getColor",
                                "(Landroid/content/Context;I)I",
                                false
                            )
                            mv.visitInsn(IRETURN)
                            val label1 = Label()
                            mv.visitLabel(label1)
                            mv.visitLocalVariable(
                                "context",
                                "Landroid/content/Context;",
                                null,
                                label0,
                                label1,
                                0
                            )
                            mv.visitLocalVariable("id", "I", null, label0, label1, 1)
                            mv.visitMaxs(3, 2)
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

                override fun visitFieldInsn(
                    opcode: Int,
                    owner: String,
                    name: String,
                    desc: String
                ) {
                    super.visitFieldInsn(opcode, owner, name, desc)
                }
            }
        }
        return mv
    }
}