package solution.views.map;

import scotlandyard.Ticket;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MapNodePopup {

    private static final int SHALLOW_BUTTON_SIZE = 25;
    private static final int BUTTON_SIZE = 50;
    private static final int TRIANGLE_SIZE = 10;
    private static final int BUTTON_PADDING = 10;
    private static final int BUTTON_CORNER_RADIUS = 15;

    private final PopupInterface mInterface;
    private final Image boatImage;
    private final Image taxiImage;
    private final Image trainImage;
    private final Image busImage;

    final ArrayList<Ticket> fullTicketList = new ArrayList<Ticket>() {{
        add(Ticket.Bus);
        add(Ticket.SecretMove);
        add(Ticket.Taxi);
        add(Ticket.Underground);
    }};

    Set<Ticket> ticketList = new HashSet<Ticket>();
    private MapPosition mapPosition;
    private boolean mDoubleMove;
    private Rectangle2D.Double mainRect;
    private Rectangle2D.Double confirmOkRect;
    private Rectangle2D.Double confirmDoubleRect;
    private ArrayList<Rectangle2D> mTicketRectList;
    private Polygon mTrianglePolygon;
    private Rectangle2D mSelectedRect;
    private Rectangle2D mHoveredRect;
    private Ticket mSelectedTicket;
    private boolean isShowing;

    public MapNodePopup(PopupInterface popupInterface) throws URISyntaxException, IOException {

        mInterface = popupInterface;

        final ClassLoader classLoader = getClass().getClassLoader();

        busImage = loadImage(new File(classLoader.getResource("imgs/actual_bus.png").toURI()));
        taxiImage = loadImage(new File(classLoader.getResource("imgs/actual_taxi.png").toURI()));
        trainImage = loadImage(new File(classLoader.getResource("imgs/actual_train.png").toURI()));
        boatImage = loadImage(new File(classLoader.getResource("imgs/actual_boat.png").toURI()));
    }

    public void reset() {
        isShowing = false;
        mapPosition = null;
        ticketList = null;
        mDoubleMove = false;
        mainRect = null;
        confirmOkRect = null;
        confirmDoubleRect = null;
        mTicketRectList = null;
        mTrianglePolygon = null;
        mSelectedRect = null;
        mHoveredRect = null;
        mSelectedTicket = null;
    }

    public void create(MapPosition mapPosition, final Dimension canvasSize, boolean doubleMove, ArrayList<Ticket> availableTickets) {
        this.mapPosition = mapPosition;
        ticketList = new HashSet<Ticket>(availableTickets);
        mDoubleMove = doubleMove;
        isShowing = true;
        init(mapPosition.getX(), mapPosition.getY(), canvasSize);
    }

    private Image loadImage(File file) throws IOException {
        final BufferedImage image = ImageIO.read(file);
        float scale = (BUTTON_SIZE * 0.8f) / (float) image.getHeight();
        final int targetImageWidth = (int) (image.getWidth() * scale);
        final int targetImageHeight = (int) (image.getHeight() * scale);
        return image.getScaledInstance(targetImageWidth, targetImageHeight, 0);
    }

    private void init(final int x, final int y, final Dimension canvasSize) {


        int width = BUTTON_PADDING + fullTicketList.size() * (BUTTON_SIZE + BUTTON_PADDING) + TRIANGLE_SIZE;
        int height = BUTTON_SIZE + BUTTON_PADDING + SHALLOW_BUTTON_SIZE + 2 * BUTTON_PADDING + TRIANGLE_SIZE;

        int xPosition;
        int yPosition;
        int triangleX1;
        int triangleX2;
        int triangleY1;
        int triangleY2;

        if (x < width / 2) {
            //to right
            xPosition = x + TRIANGLE_SIZE;
            yPosition = y - height / 2;
            triangleX1 = x + TRIANGLE_SIZE;
            triangleX2 = x + TRIANGLE_SIZE;
            triangleY1 = y + TRIANGLE_SIZE;
            triangleY2 = y - TRIANGLE_SIZE;
        } else if (canvasSize.width - x < width / 2) {
            //to left
            xPosition = x - width - TRIANGLE_SIZE;
            yPosition = y - height / 2;
            triangleX1 = x - TRIANGLE_SIZE;
            triangleX2 = x - TRIANGLE_SIZE;
            triangleY1 = y + TRIANGLE_SIZE;
            triangleY2 = y - TRIANGLE_SIZE;
        } else if (y > height) {
            //above
            xPosition = x - width / 2;
            yPosition = y - height - TRIANGLE_SIZE;
            triangleX1 = x + TRIANGLE_SIZE;
            triangleX2 = x - TRIANGLE_SIZE;
            triangleY1 = y - TRIANGLE_SIZE;
            triangleY2 = y - TRIANGLE_SIZE;
        } else {
            //below
            xPosition = x - width / 2;
            yPosition = y + TRIANGLE_SIZE;
            triangleX1 = x + TRIANGLE_SIZE;
            triangleX2 = x - TRIANGLE_SIZE;
            triangleY1 = y + TRIANGLE_SIZE;
            triangleY2 = y + TRIANGLE_SIZE;
        }

        mainRect = new Rectangle2D.Double(xPosition, yPosition, width, height);

        mTicketRectList = new ArrayList<Rectangle2D>();

        for (int i = 0; i < fullTicketList.size(); i++) {
            mTicketRectList.add(new Rectangle2D.Double(xPosition + BUTTON_PADDING + i * (BUTTON_SIZE + BUTTON_PADDING), yPosition + BUTTON_PADDING, BUTTON_SIZE, BUTTON_SIZE));
        }

        final int confirmButtonsY = yPosition + BUTTON_PADDING * 2 + BUTTON_SIZE;
        if (mDoubleMove) {
            final int smallButtonWidth = width / 2 - 2 * BUTTON_PADDING;
            confirmDoubleRect = new Rectangle2D.Double(xPosition + BUTTON_PADDING, confirmButtonsY, smallButtonWidth, SHALLOW_BUTTON_SIZE);
            confirmOkRect = new Rectangle2D.Double(xPosition + width / 2 + BUTTON_PADDING, confirmButtonsY, smallButtonWidth, SHALLOW_BUTTON_SIZE);
        } else {
            final int largeButtonWidth = width - 2 * BUTTON_PADDING;
            confirmOkRect = new Rectangle2D.Double(xPosition + BUTTON_PADDING, confirmButtonsY, largeButtonWidth, SHALLOW_BUTTON_SIZE);
        }

        mTrianglePolygon = new Polygon(new int[]{x, triangleX1, triangleX2}, new int[]{y, triangleY1, triangleY2}, 3);

    }

    public void draw(final Graphics2D g2d) {

        if (!isShowing) {
            return;
        }
        FontMetrics fm = g2d.getFontMetrics();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

        g2d.setColor(Color.BLACK);

        //first background
        g2d.fillRoundRect((int) mainRect.getX(), (int) mainRect.getY(), (int) mainRect.getWidth(), (int) mainRect.getHeight(), BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);


        //transport buttons
        for (int i = 0; i < fullTicketList.size(); i++) {
            final Ticket ticket = fullTicketList.get(i);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            if (!ticketList.contains(ticket)) {
                g2d.setColor(new Color(134, 134, 134));
            } else {
                g2d.setColor(Color.WHITE);
            }

            g2d.fillRoundRect((int) mTicketRectList.get(i).getX(), (int) mTicketRectList.get(i).getY(), (int) mTicketRectList.get(i).getWidth(), (int) mTicketRectList.get(i).getHeight(), BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);

            if (!ticketList.contains(ticket)) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            } else {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            Image image = null;

            switch (ticket) {
                case Bus:
                    image = busImage;
                    break;
                case Taxi:
                    image = taxiImage;
                    break;
                case Underground:
                    image = trainImage;
                    break;
                case SecretMove:
                    image = boatImage;
                    break;
            }

            g2d.drawImage(image, ((int) mTicketRectList.get(i).getCenterX() - image.getWidth(null) / 2), ((int) mTicketRectList.get(i).getCenterY() - image.getHeight(null) / 2), null);


        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        if (mSelectedRect != null) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(new Color(134, 134, 134));
        }

        //confirmation buttons
        if (mDoubleMove) {

            g2d.fillRoundRect((int) confirmOkRect.getX(), (int) confirmOkRect.getY(), (int) confirmOkRect.getWidth(), (int) confirmOkRect.getHeight(), BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);

            g2d.fillRoundRect((int) confirmDoubleRect.getX(), (int) confirmDoubleRect.getY(), (int) confirmDoubleRect.getWidth(), (int) confirmDoubleRect.getHeight(), BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);

            Rectangle2D r = fm.getStringBounds("Double Move", g2d);
            int textX = (int) (confirmDoubleRect.getCenterX() - ((int) r.getWidth() / 2));
            int textY = (int) (confirmDoubleRect.getCenterY() - ((int) r.getHeight() / 2) + fm.getAscent());
            g2d.setColor(Color.BLACK);
            g2d.drawString("Double Move", textX, textY);

        } else {
            g2d.fillRoundRect((int) confirmOkRect.getX(), (int) confirmOkRect.getY(), (int) confirmOkRect.getWidth(), (int) confirmOkRect.getHeight(), BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);
        }

        Rectangle2D r = fm.getStringBounds("Ok", g2d);
        int textX = (int) (confirmOkRect.getCenterX() - ((int) r.getWidth() / 2));
        int textY = (int) (confirmOkRect.getCenterY() - ((int) r.getHeight() / 2) + fm.getAscent());
        g2d.setColor(Color.BLACK);
        g2d.drawString("Ok", textX, textY);



        g2d.setStroke(new BasicStroke(3f));

        //highlight any button being hovered
        if (mHoveredRect != null) {
            g2d.setColor(new Color(182, 182, 182, 205));
            g2d.drawRoundRect((int) mHoveredRect.getX(), (int) mHoveredRect.getY(), (int) mHoveredRect.getWidth(), (int) mHoveredRect.getHeight(), BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);
        }

        g2d.setStroke(new BasicStroke(4f));

        //highlight and button currently clicked
        if (mSelectedRect != null) {
            g2d.setColor(new Color(14, 220, 0, 205));
            g2d.drawRoundRect((int) mSelectedRect.getX(), (int) mSelectedRect.getY(), (int) mSelectedRect.getWidth(), (int) mSelectedRect.getHeight(), BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);
        }

        g2d.setColor(Color.BLACK);

        g2d.fillPolygon(mTrianglePolygon);


    }

    public boolean onClick(final int x, final int y) {

        if (!isShowing) {
            return false;
        }

        if (mainRect.contains(x, y)) {
            if (confirmOkRect.contains(x, y)) {
                mInterface.onTicketSelected(mSelectedTicket, mapPosition.getId());
            } else if (confirmDoubleRect != null && confirmDoubleRect.contains(x, y)) {
                mInterface.onDoubleMoveSelected(mSelectedTicket, mapPosition.getId());
            } else {
                for (int i = 0; i < mTicketRectList.size(); i++) {
                    Rectangle2D rect = mTicketRectList.get(i);
                    Ticket ticket = fullTicketList.get(i);
                    if (ticketList.contains(ticket) && rect.contains(x, y)) {
                        mSelectedTicket = ticket;
                        mSelectedRect = rect;
                        return true;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean onMouseMoved(int x, int y) {

        if (!isShowing) {
            return false;
        }

        if (mainRect.contains(x, y)) {
            mHoveredRect = null;
            if (mSelectedRect != null && confirmOkRect != null && confirmOkRect.contains(x, y)) {
                mHoveredRect = confirmOkRect;
            } else if (mSelectedRect != null && confirmDoubleRect != null && confirmDoubleRect.contains(x, y)) {
                mHoveredRect = confirmDoubleRect;
            } else {
                for (int i = 0; i < mTicketRectList.size(); i++) {
                    Rectangle2D rect = mTicketRectList.get(i);
                    Ticket ticket = fullTicketList.get(i);
                    if (ticketList.contains(ticket) && rect.contains(x, y)) {
                        mHoveredRect = rect;
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }


    public interface PopupInterface {
        public void onTicketSelected(final Ticket ticket, final int nodeId);

        public void onDoubleMoveSelected(final Ticket ticket, final int nodeId);
    }

}
