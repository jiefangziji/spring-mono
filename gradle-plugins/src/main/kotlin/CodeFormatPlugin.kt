import com.diffplug.gradle.spotless.SpotlessApply
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

class CodeFormatPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(SpotlessPlugin::class)
        target.plugins.apply(CustomRepositoryPlugin::class)

        target.extensions.configure<SpotlessExtension> {
            if (target.plugins.hasPlugin(JavaPlugin::class)){
                java {
                    palantirJavaFormat()
                    removeUnusedImports()
                    formatAnnotations()
                }
            }

            if (target.extensions.findByType(KotlinJvmProjectExtension::class) != null) {
                kotlin {
                    ktfmt().googleStyle()
                }
            }

            if (target.parent?.tasks?.findByName("spotlessApply") == null) {
                target.parent?.tasks?.register("spotlessApply")
            }
            target.parent?.tasks?.getByName("spotlessApply"){
                dependsOn(target.tasks.getByName("spotlessApply"))
            }
        }

    }
}
