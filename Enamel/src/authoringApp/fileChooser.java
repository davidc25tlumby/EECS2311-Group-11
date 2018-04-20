package authoringApp;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.io.FilenameUtils;


public class fileChooser {
	

	public static File saveFileChooser(File currentDir, String ext) {
		JFileChooser fc= new JFileChooser();
		
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
				return fc.getSelectedFile();
			}
		}
		return null;
	}
	
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
				return fc.getSelectedFile();
			}
		}
		return null;
	}

	

}
