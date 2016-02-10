package com.beust.kobalt.plugin.android

import com.beust.kobalt.Variant
import com.beust.kobalt.api.Project
import com.beust.kobalt.maven.MavenId
import com.beust.kobalt.misc.KFiles

class AndroidFiles {
    companion object {
        fun intermediates(project: Project) = KFiles.joinDir(project.directory, project.buildDirectory,
                "intermediates")

        fun manifest(project: Project) =
                KFiles.joinDir(project.directory, "src", "main", "AndroidManifest.xml")

        fun mergedManifest(project: Project, variant: Variant) : String {
            val dir = KFiles.joinAndMakeDir(intermediates(project), "manifests", "full", variant.toIntermediateDir())
            return KFiles.joinDir(dir, "AndroidManifest.xml")
        }

        fun mergedResourcesNoVariant(project: Project) =
                KFiles.joinAndMakeDir(AndroidFiles.intermediates(project), "res", "merged")

        fun mergedResources(project: Project, variant: Variant) =
                KFiles.joinAndMakeDir(mergedResourcesNoVariant(project), variant.toIntermediateDir())

        fun exploded(project: Project, mavenId: MavenId) = KFiles.joinAndMakeDir(
                intermediates(project), "exploded-aar", mavenId.groupId, mavenId.artifactId, mavenId.version!!)

        fun explodedManifest(project: Project, mavenId: MavenId) =
                KFiles.joinDir(exploded(project, mavenId), "AndroidManifest.xml")

        fun aarClassesJar(dir: String) = KFiles.joinDir(dir, "classes.jar")

        fun explodedClassesJar(project: Project, mavenId: MavenId) = aarClassesJar(
                KFiles.joinDir(exploded(project, mavenId)))

        fun temporaryApk(project: Project, flavor: String)
                = KFiles.joinFileAndMakeDir(AndroidFiles.intermediates(project), "res", "resources$flavor.ap_")

        /**
         * Use the android home define on the project if any, otherwise use the environment variable.
         */
        fun androidHomeNoThrows(project: Project?, config: AndroidConfig?): String? {
            var result = System.getenv("ANDROID_HOME")
            if (project != null && config?.androidHome != null) {
                result = config?.androidHome
            }

            return result
        }

        fun androidHome(project: Project?, config: AndroidConfig) = androidHomeNoThrows(project, config) ?:
                throw IllegalArgumentException("Neither androidHome nor \$ANDROID_HOME were defined")

        fun preDexed(project: Project, variant: Variant) =
                KFiles.joinAndMakeDir(intermediates(project), "pre-dexed", variant.toIntermediateDir())

        fun intermediatesClasses(project: Project, vararg dir: String)
                = KFiles.joinAndMakeDir(intermediates(project), "classes", *dir)
    }
}
