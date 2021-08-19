package com.magic.multi.theme.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Denotes that the annotated method should only be called after set MultiThemeFactory as LayoutInflater Factory
 * <p>
 * Example:
 * <pre><code>
 * registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
 * override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
 *              super.onActivityPreCreated(activity, savedInstanceState)
 *              activity.layoutInflater.factory =  MultiThemeFactory()
 * }
 * override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
 *             (activity as? AppCompatActivity)?.let {
 *                      SkinLoadManager.getInstance().bindPage(it)
 *             }
 * }
 * })
 * </code></pre>
 */
@Documented
@Retention(CLASS)
@Target({METHOD})
public @interface CalledAfterSetThemeFactory {
}