package com.magic.theme.resource.proxy

import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.*


/**
 * Created by mistletoe
 * on 2021/9/1
 **/
class ReplaceResourceClassVisitor(private val className: String?, classVisitor: ClassVisitor?) :
    ClassVisitor(Opcodes.ASM6, classVisitor) {

    companion object {
        val SYSTEM_RESOURCE_CLASS_LIST = listOf(
            "androidx/core/content/ContextCompat",
            "androidx/appcompat/content/res/AppCompatResources"
        )
        const val GET_DRAWABLE_METHOD_NAME = "getDrawable"
        const val GET_COLOR_METHOD_NAME = "getColor"
        const val GET_COLOR_STATE_LIST_METHOD_NAME = "getColorStateList"
    }

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
        if (SYSTEM_RESOURCE_CLASS_LIST.contains(className)) {
            println("meeting target  class:$className")
            mv = object : AdviceAdapter(ASM6, mv, access, name, desc) {
                override fun visitCode() {
                    when (name) {
                        GET_DRAWABLE_METHOD_NAME -> {
                            println("meeting $className getDrawable function")
                            mv.visitFieldInsn(
                                GETSTATIC,
                                "com/magic/resource/resource/core/action/SkinLoadManager",
                                "Companion",
                                "Lcom/magic/resource/resource/core/action/SkinLoadManager\$Companion;"
                            )
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/resource/resource/core/action/SkinLoadManager\$Companion",
                                "getInstance",
                                "()Lcom/magic/resource/resource/core/action/SkinLoadManager;",
                                false
                            )
                            mv.visitVarInsn(ILOAD, 1)
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/resource/resource/core/action/SkinLoadManager",
                                "getDrawable",
                                "(I)Landroid/graphics/drawable/Drawable;",
                                false
                            )
                            mv.visitInsn(ARETURN)
                            mv.visitMaxs(2, 2)
                            mv.visitEnd()
                            println("meeting $className  getDrawable function,replace finished")
                        }
                        GET_COLOR_METHOD_NAME -> {
                            println("meeting $className  getColor function")
                            mv.visitFieldInsn(
                                GETSTATIC,
                                "com/magic/resource/resource/core/action/SkinLoadManager",
                                "Companion",
                                "Lcom/magic/resource/resource/core/action/SkinLoadManager\$Companion;"
                            )
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/resource/resource/core/action/SkinLoadManager\$Companion",
                                "getInstance",
                                "()Lcom/magic/resource/resource/core/action/SkinLoadManager;",
                                false
                            )
                            mv.visitVarInsn(ILOAD, 1)
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/resource/resource/core/action/SkinLoadManager",
                                "getColor",
                                "(I)I",
                                false
                            )
                            mv.visitInsn(IRETURN)
                            mv.visitMaxs(2, 2)
                            mv.visitEnd()
                            println("meeting $className  getColor function,replace finished")
                        }
                        GET_COLOR_STATE_LIST_METHOD_NAME -> {
                            println("meeting $className  getColorStateList function")
                            mv.visitFieldInsn(
                                GETSTATIC,
                                "com/magic/resource/resource/core/action/SkinLoadManager",
                                "Companion",
                                "Lcom/magic/resource/resource/core/action/SkinLoadManager\$Companion;"
                            )
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/resource/resource/core/action/SkinLoadManager\$Companion",
                                "getInstance",
                                "()Lcom/magic/resource/resource/core/action/SkinLoadManager;",
                                false
                            )
                            mv.visitVarInsn(ILOAD, 1)
                            mv.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "com/magic/resource/resource/core/action/SkinLoadManager",
                                "getColorStateList",
                                "(I)Landroid/content/res/ColorStateList;",
                                false
                            );
                            mv.visitInsn(IRETURN)
                            mv.visitMaxs(2, 2)
                            mv.visitEnd()
                            println("meeting $className  getColorStateList function,replace finished")
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