package com.freedomotic.clients.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import org.vectomatic.dom.svg.ui.SVGResource;

import java.awt.*;

public interface FreedomoticBundle extends ClientBundle {
    public static FreedomoticBundle INSTANCE = GWT.create(FreedomoticBundle.class);

    @Source("logo_ivan_vector_trazo.svg")
    SVGResource logo();

}