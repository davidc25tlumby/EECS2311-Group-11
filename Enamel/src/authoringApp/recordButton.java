package authoringApp;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class recordButton extends JLabel {

	  public recordButton(String msg) {

	    super();
	    try {
        	Image img = ImageIO.read(getClass().getResource(msg));
        	setIcon(new ImageIcon(img));
        } catch (Exception ex) {
        	System.out.println(ex);
        	System.out.println("s");
       	}
	    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    addMouseListener(new MouseAdapter() {
	      public void mouseClicked(MouseEvent me) {
	        fireActionPerformed(new ActionEvent(recordButton.this, ActionEvent.ACTION_PERFORMED,
	            "SecretMessage"));
	      }
	    });
	  }

	  public void addActionListener(ActionListener l) {
	    listenerList.add(ActionListener.class, l);
	  }

	  public void removeActionListener(ActionListener l) {
	    listenerList.remove(ActionListener.class, l);
	  }

	  protected void fireActionPerformed(ActionEvent ae) {

	    Object[] listeners = listenerList.getListeners(ActionListener.class);

	    for (int i = 0; i < listeners.length; i++) {
	      ((ActionListener) listeners[i]).actionPerformed(ae);
	    }
	  }
	}