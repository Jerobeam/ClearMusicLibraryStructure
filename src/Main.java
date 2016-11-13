import java.io.File;

public class Main {

	public static void main(String[] args) {

		String musicDir = "D:/Musik/PLs/Hip Hop ist deswegen noch lange keine Bitch" + "/";
//		String musicDir = "C:/Users/Sebastian/Desktop/Musik" + "/";
		int errorCount = 0;

		File folder = new File(musicDir);
		String folderName;

		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {

				folderName = fileEntry.getName();

				if (folderName.toLowerCase().contains("feat.")) {

					String[] parts = folderName.split(" feat. ", 2);
					String artist = parts[0];

					File artistFolder = new File(musicDir + artist);

					// Check if folder without "feat." exists, if not create it
					if (!(artistFolder.isDirectory())) {
						boolean folderCreationSuccess = artistFolder.mkdir();
						if (!folderCreationSuccess) {
							System.out.println("Error: Artist folder'" + artistFolder.getAbsolutePath()
									+ "' could not be created.");
							errorCount++;
						}
					}

					File featFolder = new File(musicDir + folderName);
					File fileToMove;
					File checkFile;

					if (featFolder.isDirectory()) {
						for (File featFolderEntry : featFolder.listFiles()) {

							String pathFrom = featFolder.getAbsolutePath() + "/" + featFolderEntry.getName();
							String pathTo = artistFolder.getAbsolutePath() + "/" + featFolderEntry.getName();

							boolean moveSuccess = false;

							if (!featFolderEntry.isDirectory()) {
								fileToMove = new File(pathFrom);
								checkFile = new File(pathTo);
								
								// Only move file if it does not exist yet
								if(checkFile.exists() && !checkFile.isDirectory()) { 
									fileToMove.delete();
								}else{
									moveSuccess = fileToMove.renameTo(new File(pathTo));
								}

								// fileToMove.delete();
							} else if (featFolderEntry.isDirectory()) {
								for (File albumFolderEntry : featFolderEntry.listFiles()) {

									File albumFolder = new File(pathTo);
									if (!(albumFolder.isDirectory())) {
										boolean folderCreationSuccess = albumFolder.mkdir();
										if (!folderCreationSuccess) {
											System.out.println("Error: Album folder '" + albumFolder.getAbsolutePath()
													+ "' could not be created.");
											errorCount++;
										}
									}

									fileToMove = new File(pathFrom + "/" + albumFolderEntry.getName());
									checkFile = new File(pathTo + "/" + albumFolderEntry.getName());
									
									// Only move file if it does not exist yet
									if(checkFile.exists() && !checkFile.isDirectory()) { 
										fileToMove.delete();
									}else{
										moveSuccess = fileToMove.renameTo(new File(pathTo + "/" + albumFolderEntry.getName()));
									}
								}

								// Delete album-folder from feat-folder (applies
								// only, if folder is empty)
								featFolderEntry.delete();
							}

							if (!moveSuccess) {
								System.out.println("Error: File '" + pathFrom + "' could not be moved.");
								errorCount++;
							}
						}
						// Delete feat-folder (applies only, if folder is empty)
						featFolder.delete();
					}
				}
			}
		}

		if (errorCount == 0) {
			System.out.println("Done without any errors.");
		} else {
			System.out.println("Done with " + errorCount + " errors.");
		}
	}
}
