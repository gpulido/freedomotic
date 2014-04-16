package com.freedomotic.clients.client;

import com.freedomotic.clients.client.resources.FreedomoticBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.ui.SVGImage;

/**
 * Created by gpt on 10/04/14.
 */
public class MyDockLayoutPanel{
    interface MyBinder extends UiBinder<Widget, MyDockLayoutPanel> { }

    private static MyBinder ourUiBinder = GWT.create(MyBinder.class);

    @UiField
    SVGImage logo;

    public MyDockLayoutPanel() {
        //this.logo = new SVGImage();

        //DivElement rootElement = ourUiBinder.createAndBindUi(this);
        Widget rootElement = ourUiBinder.createAndBindUi(this);
        RootLayoutPanel.get().add(rootElement);

        OMSVGSVGElement svg =(OMSVGSVGElement)logo.getSvgElement();
        OMSVGRect viewBox = svg.getViewBox().getBaseVal();
        if (viewBox.getWidth() == 0 || viewBox.getHeight() == 0) {
            OMSVGRect bbox = svg.getBBox();
            bbox.assignTo(viewBox);
        }
        svg.getStyle().setWidth(300, Style.Unit.PX);
        svg.getStyle().setHeight(130, Style.Unit.PX);


/*
        final OMSVGSVGElement svg = FreedomoticBundle.INSTANCE.logo().getSvg();
        SVGImage mySvgImage = new SVGImage(svg) {
            protected void onAttach() {
                OMSVGRect viewBox = svg.getViewBox().getBaseVal();
                if (viewBox.getWidth() == 0 || viewBox.getHeight() == 0) {
                    OMSVGRect bbox = svg.getBBox();
                    bbox.assignTo(viewBox);
                }
                svg.getStyle().setWidth(300, Style.Unit.PX);
                svg.getStyle().setHeight(130, Style.Unit.PX);
                super.onAttach();
            }
        };
        mySvgImage.setStyleName("logo");
       // myImage.add(mySvgImage);
        myImage.setStyleName("header_panel");*/
//

    }
}