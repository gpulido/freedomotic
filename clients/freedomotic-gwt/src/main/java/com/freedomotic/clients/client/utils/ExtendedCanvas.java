package com.freedomotic.clients.client.utils;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by gpt on 31/03/14.
 */
public class ExtendedCanvas  implements MouseWheelHandler{

    Canvas canvas;
    Canvas backbuffer;
    Context2d ctx;
    Context2d backBufferContext;

    LinkedHashMap<String, Layer> layers = new LinkedHashMap<>();
    //we only accept one selected layer at a time.
    //TODO: Change to a structure that traverse the visible layers from top to bottom
    Layer selectedLayer = null;

    private static int BORDER_X = 10; //the empty space around the map
    private static int BORDER_Y = 10; //the empty space around the map


    private double mScaleFactor = 1;
    private double mPosX = 0;
    private double mPosY = 0;

    double totalZoom = 1;
    double offsetX = 0;
    double offsetY = 0;

    //private DockLayoutPanel parent;

    public ExtendedCanvas() {
        canvas = Canvas.createIfSupported();
        backbuffer = Canvas.createIfSupported();
        ctx = canvas.getContext2d();
        backBufferContext = backbuffer.getContext2d();

        canvas.addMouseWheelHandler(this);

    }

    public int getCanvasWitdh()
    {
        return canvas.getParent().getOffsetWidth();// - BORDER_X - 200;
    }
    public int getCanvasHeight()
    {
        return canvas.getParent().getOffsetHeight();// - BORDER_Y - 200;
    }


    void initCanvas()
    {
        layers.clear();
    }

    void setSize()
    {
        int width = getCanvasWitdh();
        int height = getCanvasHeight();

        canvas.setWidth(width + "px");
        canvas.setHeight(height + "px");
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        backbuffer.setCoordinateSpaceWidth(width);
        backbuffer.setCoordinateSpaceHeight(height);
        for(Layer layer: layers.values())
        {
            layer.setSize();
        }

    }

    void addDrawingElement(DrawableElement de, Layer layer)
    {
        //TODO: search for the layer and add the element
        layer.addObjectToLayer(de);
        de.setParentCanvas(this);
    }

    DrawableElement elementUnderMouse;
    void registerHandlers()
    {
        canvas.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (selectedLayer!= null) {
                    final DrawableElement de = selectedLayer.getElementUnderCoordinates(event.getX(), event.getY());
                    if (de != null) {
                        de.OnClick(canvas);
                    }
                }

            }
        });

        canvas.addDoubleClickHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                if (selectedLayer != null) {
                    final DrawableElement de = selectedLayer.getElementUnderCoordinates(event.getX(), event.getY());
                    if (de != null) {
                        if (de.OnDoubleClick(canvas))
                            return;
                    }
                    fitToScreen(getCanvasWitdh(),getCanvasHeight(), getCanvasWitdh()/2, getCanvasHeight() /2);

                }
                //TODO: this is wrong here. The extendedCanvas doesn't have to know about what the doubleclick does
                //Find where to move this
                //setSize();
                //fitToScreen(getCanvasWitdh(), getCanvasHeight(), 0, 0);
            }
        });

        canvas.addMouseMoveHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
                if (selectedLayer != null) {
                    final DrawableElement de = getElementUnderCoordinates(event.getX(), event.getY());
                    if ((de == null && elementUnderMouse != null) || (de != null && elementUnderMouse != null && de != elementUnderMouse))
                        elementUnderMouse.OnMouseLeft(canvas);
                    if (de != null) {
                        de.OnMouseOver(canvas);
                        elementUnderMouse = de;
                    }
                }
            }
        });

    }

    public void onMouseWheel(MouseWheelEvent event) {
        int move = event.getDeltaY();

        double xPos = (event.getRelativeX(canvas.getElement()));
        double yPos = (event.getRelativeY(canvas.getElement()));


        double scale = 1;
        double zoom;
        if (move < 0) {
            scale = mScaleFactor *1.1;
            zoom = 1.1;
        } else {
            scale = mScaleFactor / 1.1;
            zoom = 1 / 1.1;
        }

        double newX = xPos / mScaleFactor;
        double newY = yPos / mScaleFactor;

        double xPosition = (-newX * scale) + newX;
        double yPosition = (-newY * scale) + newY;

        centerAndScale(xPosition, yPosition, scale, true);

        //offsetX += (xPosition * mScaleFactor);
        //offsetY += (mPosY * mScaleFactor);


       // backContext.clearRect(0, 0, width, height);

       // backContext.translate(xPosition, yPosition);

       // backContext.scale(zoom, zoom);



        //totalZoom = totalZoom * mScaleFactor;
        //Invalidate();
        //buffer(backContext, context);
    }



    boolean invalid = true;
    void draw() {
        if (invalid) {
            backBufferContext.clearRect(0, 0, getCanvasWitdh(), getCanvasHeight());

            for (Layer layer : layers.values()) {
                layer.draw();
            }
            invalid = false;
        }
        ctx.clearRect(0, 0, getCanvasWitdh(), getCanvasHeight());
        ctx.drawImage(backBufferContext.getCanvas(), 0, 0);

    }
    public void Invalidate()
    {
        invalid = true;

    }

    void updateElements()
    {
        for(Layer layer: layers.values())
        {
            layer.updateElements();
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }


    public Context2d getContext()
    {
        return backBufferContext;
    }


    //Adapt the "original coordinates" from freedomotic to the canvas size
    public void fitToScreen(double width, double height, double posX, double posY) {
        //TODO: check for a maximum scale
        double xSize = getCanvasWitdh();
        double ySize = getCanvasHeight();

        double xScale = xSize / width;
        double yScale = ySize / height;
        double centerCanvasScaledX = (getCanvasWitdh() / 2);
        double centerCanvasScaledY = (getCanvasHeight() / 2);

        double scale;
        if (xScale < yScale) {
            scale = xScale;
        }
        else
            scale = yScale;

        double centerX =  centerCanvasScaledX / scale - posX;
        double centerY = centerCanvasScaledY / scale - posY;
        centerAndScale(centerX, centerY, scale, true);
        //updateElements();

    }
    public void centerAndScale(double posX, double posY, double scale, boolean animation)
    {
        MoveWithAnimation moveAnimation = new MoveWithAnimation(mPosX, mPosY, posX, posY, mScaleFactor, scale);
        moveAnimation.run(500);

    }


    public double getScaleFactor() {
        return mScaleFactor;
    }
    public double getPosX() {
        return mPosX;
    }

    public double getPosY() {
        return mPosY;
    }


    //region Layers Management

    public Layer addLayer(String objectUUID, String name)
    {
        Layer newLayer = new Layer(this, objectUUID);
        newLayer.setName(name);
        layers.put(objectUUID, newLayer);
        //TODO: rename setSize for a more appropiate name
        newLayer.setSize();
        return newLayer;

    }

    public void changeLayerVisibility(String objectUUID, boolean visibility)
    {
        Layer layer = layers.get(objectUUID);
        if (visibility == true)
            selectedLayer = layer;
        layer.setVisible(visibility);
    }

    public DrawableElement getElementUnderCoordinates(int x, int y)
    {
        //TODO: this method should be changed to search from top to bottom in the visible layers
        /*ListIterator<Layer> iter = (ListIterator<Layer>) layers.values().iterator();
        while(iter.hasPrevious())
        {
            Layer layer = iter.previous();
            if (layer.isVisible()) {
                DrawableElement de = layer.getElementUnderCoordinates(x, y);
                if (de != null) ;
                {
                    return de;

                }
            }
        }
        return null;*/
        return selectedLayer.getElementUnderCoordinates(x, y);

    }

    public List<LayerPojo> getLayers() {
        ArrayList<LayerPojo> layersPojos = new ArrayList<>();
        for (Layer layer : layers.values()) {
            layersPojos.add(layer.getLayer());

        }
        return layersPojos;
    }

    //endregion

    //region Animations
    public class MoveWithAnimation  extends Animation {
        // initial position
        private double startX;
        private double startY;
        private double endX;
        private double endY;
        private double vectorX;
        private double vectorY;
        private double startScale;
        private double endScale;
        private boolean animateX;
        private boolean animateY;
        private boolean animateScale;

        public MoveWithAnimation(double startX, double startY, double endX, double endY, double startScale, double endScale) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.startScale = startScale;
            this.endScale = endScale;
            animateX = (startX != endX);
            animateY = (startY != endY);
            animateScale = (startScale != endScale);



        }
        @Override
        protected void onUpdate(double progress) {
            if (animateX) mPosX = extractProportionalValue(progress, startX, endX);
            if (animateY) mPosY = extractProportionalValue(progress, startY, endY);
            if (animateScale) mScaleFactor = extractProportionalValue(progress, startScale, endScale);
            invalid = true;
        }

        private double extractProportionalValue(double progress, double start, double end) {
            double out = start - (start - end) * progress;
            return out;
        }

    }

    //endregion



}

