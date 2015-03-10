package solution.views;

import solution.Models.CoordinateData;
import solution.Models.GraphData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class deals with drawing the underlying Graph and anything on top of
 * it.
 */
public class GraphView extends JPanel {
    private static final int CIRC_RADIUS = 20;
    private final ArrayList<CoordinateData> mNodes;
    private final Color mDrawColour;
    private BufferedImage mGraphImage;
    private Dimension mImageSize;
    private GraphViewListener mListener;
    private int mCurrentHoverNode;
    private ArrayList<Integer> availablePositions;



    public interface GraphViewListener {
        public void onNodeClicked(int nodeId);
    }

    public GraphView(final String graphImageMapPath, final GraphData graphData) {
        if (graphImageMapPath == null || graphData == null) {
            System.err.println("Do not pass null variables!");
        }
        mNodes = graphData.getNodes();
        availablePositions = new ArrayList<Integer>();
        mDrawColour = Color.gray;
        addMouseListener(new GraphMouseListener());
        addMouseMotionListener(new GraphMouseListener());
        setupGraphImage(graphImageMapPath);
    }

    public void setListener(GraphViewListener mListener) {
        this.mListener = mListener;
    }

    public void setAvailablePositions(ArrayList<Integer> availablePositions) {
        this.availablePositions = availablePositions;
    }

    private void setupGraphImage(final String graphImageMapPath) {
        try {
            final URL resource = getClass().getClassLoader().getResource(graphImageMapPath);
            mGraphImage = ImageIO.read(new File(resource.toURI()));
            mImageSize = new Dimension(mGraphImage.getWidth(), mGraphImage.getHeight());
            setSize(mImageSize);
            setPreferredSize(mImageSize);
        } catch (IOException ex) {
            //todo handle exception...
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(mGraphImage, null, 0, 0);

        g2d.setColor(mDrawColour);


        for(CoordinateData coordinateData : mNodes){
            final boolean isHovered = coordinateData.getId() == mCurrentHoverNode;
            final boolean isAvailable = availablePositions.contains(coordinateData.getId());

            int radius = isHovered && isAvailable ? (int) (CIRC_RADIUS * 1.5f) : CIRC_RADIUS;

            if(isAvailable){
                g2d.setColor(Color.RED);
                g2d.fillOval(coordinateData.getX()-radius/2, coordinateData.getY()-radius/2, radius, radius);
                g2d.setColor(mDrawColour);
            }else{
                g2d.fillOval(coordinateData.getX()-radius/2, coordinateData.getY()-radius/2, radius, radius);
            }

        }

    }

    class GraphMouseListener extends MouseAdapter implements MouseMotionListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(mListener != null) {
                for (CoordinateData coordinateData : mNodes) {
                    if (new Rectangle2D.Double(coordinateData.getX() - CIRC_RADIUS, coordinateData.getY() - CIRC_RADIUS, 2 * CIRC_RADIUS, 2 * CIRC_RADIUS).contains(e.getX(), e.getY())) {
                        mListener.onNodeClicked(coordinateData.getId());
                    }
                    return;
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            for (CoordinateData coordinateData : mNodes) {
                if (new Rectangle2D.Double(coordinateData.getX() - CIRC_RADIUS, coordinateData.getY() - CIRC_RADIUS, 2 * CIRC_RADIUS, 2 * CIRC_RADIUS).contains(e.getX(), e.getY())) {
                    mCurrentHoverNode = coordinateData.getId();
                    repaint();
                    return;
                }
            }
            if(mCurrentHoverNode != -1){
                mCurrentHoverNode = -1;
                repaint();
            }
        }


    }

}
