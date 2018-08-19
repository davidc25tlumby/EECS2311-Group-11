package authoringApp;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.io.FilenameUtils;

/**
 * Opens a directory interface to control saving and loading operations.
 * 
 * @author Xiahan Chen, Huy Hoang Minh Cu, Qasim Mahir
 */
public class fileChooser {

	/**
	 * Opens a FileChooser to save a file.
	 * 
	 * @param currentDir
	 *            The directory to open the chooser at.
	 * @param ext
	 *            Expected extension for the file.
	 * @return The file name to save as, otherwise null (do not save).
	 */
	public static File saveFileChooser(File currentDir, String ext) {
		JFileChooser fc = new JFileChooser();

		fc.setCurrentDirectory(currentDir);
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String selectedExt = FilenameUtils.getExtension(fc.getSelectedFile().getName());
			if (!ext.equals(selectedExt)) {
				JPanel errorPanel = new JPanel();
				JOptionPane.showMessageDialog(errorPanel, "Could not save file, Wrong file type", "Error",
						JOptionPane.ERROR_MESSAGE);
				return null;
			} else {
				System.out.println("close");
				return fc.getSelectedFile();
			}
		}
		return null;
	}

	/**
	 * Opens a FileChooser to load a file.
	 * 
	 * @param currentDir
	 *            The directory to open the chooser at.
	 * @param ext
	 *            Expected extension for the file.
	 * @return The file to load, otherwise null (do not save).
	 */
	public static File openFileChooser(File currentDir, String ext) {
		JFileChooser fc = new JFileChooser();

		fc.setCurrentDirectory(currentDir);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String selectedExt = FilenameUtils.getExtension(fc.getSelectedFile().getName());
			if (!ext.equals(selectedExt)) {
				JPanel errorPanel = new JPanel();
				JOptionPane.showMessageDialog(errorPanel, "Could not open file, Wrong file type", "Error",
						JOptionPane.ERROR_MESSAGE);
				return null;
			} else {
				System.out.println("open");
				return fc.getSelectedFile();
			}
		}
		return null;
	}
}
