package com.ebinjoy999.buildhealth

import org.gradle.api.Project
import org.gradle.api.provider.Property

/**
 * Extension for configuring Build Health plugin behavior.
 */
abstract class BuildHealthExtension {

    /**
     * Enable JSON export of build metrics.
     * Default: false
     *
     * Can also be set via command line: -PbuildHealth.exportJson=true
     */
    abstract val exportJson: Property<Boolean>

    /**
     * Number of slowest tasks to display in the summary.
     * Default: 5
     */
    abstract val slowestTaskCount: Property<Int>

    companion object {
        const val EXTENSION_NAME = "buildHealth"
        const val PROPERTY_EXPORT_JSON = "buildHealth.exportJson"

        /**
         * Resolves the effective exportJson value from extension or project property.
         */
        fun resolveExportJson(extension: BuildHealthExtension, project: Project): Boolean {
            // Command line property takes precedence
            val propertyValue = project.findProperty(PROPERTY_EXPORT_JSON)?.toString()
            if (propertyValue != null) {
                return propertyValue.equals("true", ignoreCase = true)
            }
            return extension.exportJson.getOrElse(false)
        }
    }
}
