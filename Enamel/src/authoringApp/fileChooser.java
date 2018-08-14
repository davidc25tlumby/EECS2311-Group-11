package authoringApp;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.io.FilenameUtils;


public class fileChooser {
	

	public static File saveFileChooser(File currentDir, String ext) {
		JFileChooser fc = new JFileChooser();
		
		fc.setCurrentDirectory(currentDir);
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String selectedExt = FilenameUtils.getExtension(fc.getSelectedFile().getName());
			if (!ext.equals(selectedExt)) {
				/* final JPanel */JPanel errorPanel = new JPanel();
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
	 * Opens a FileChooser interface for the user to select a file.
	 * 
	 * @param currentDir
	 *            The directory that will first appear on the FileChooser.
	 * @param ext
	 *            Expected file extension to be returned.
	 * @return The file selected by the user and is of the appropriate
	 *         extension, otherwise null.
	 */
	/**
	 * 
	 * @param currentDir The default directory to open the fileChooser at.
	 * @param ext The expected extension of the file chosen by the user.
	 * @return The file selected by the user. If the file is invalid or if the user cancels, returns null.
	 */

	
	/**
	 * 
	 * @return The text that is typed into the JTextField.
	 */
	 
	public static File openFileChooser(File currentDir, String ext) {
		JFileChooser fc= new JFileChooser();

		fc.setCurrentDirectory(currentDir);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String selectedExt =  FilenameUtils.getExtension(fc.getSelectedFile().getName());
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
