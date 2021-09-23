package com.maukaim.blob.swagger.config;

import springfox.documentation.spring.web.plugins.Docket;

@FunctionalInterface
public interface DocketCustomizer {

    /**
     *  Allow to customize the docket before it is returned.
     *
     * @param swaggerProperties , the docket to customize.
     */
    void customize(Docket docket);
}
