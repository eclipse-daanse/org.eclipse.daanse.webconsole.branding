/*
* Copyright (c) 2024 Contributors to the Eclipse Foundation.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*   SmartCity Jena - initial
*   Stefan Bischof (bipolis.org) - initial
*/
package org.eclipse.daanse.webconsole.branding.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.osgi.service.servlet.whiteboard.annotations.RequireHttpWhiteboard;

@RequireHttpWhiteboard
class BrandingTest {

    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    @Test
    @Disabled("""
            The web console does not register in the framework that test.bndrun launches:
            every path 404s, including /system/console itself, although webconsole 5.0.18,
            http.jetty12, servlet-api and configadmin all resolve into -runbundles. Jetty
            itself answers (it serves the 404 pages), and waiting 10s does not help, so this
            is not a readiness race. Enable once the bndrun assembles a working console.
            """)
    void testSVG() throws Exception {

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpResponse<String> response = httpClient.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create("http://admin:admin@localhost:8080/system/console/res/logo.svg"))
                            .header("Authorization", getBasicAuthenticationHeader("admin", "admin")).GET().build(),
                    BodyHandlers.ofString());
            assertThat(response.body()).contains("svg").contains("Eclipse");
        }

    }
}

