package com.coffeecat.springbootcourse.service;

import com.coffeecat.springbootcourse.exceptions.ImageTooSmallException;
import com.coffeecat.springbootcourse.exceptions.InvalidFileException;
import com.coffeecat.springbootcourse.model.dto.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class FileService {

    //get list of valid extensions for image-files:
    @Value("${photo.file.extensions}")
    private String validExtensions;

    //generate Random-Number(0-999)
    private Random random = new Random();

    private String getFileExtension(String filename) {
        int dotPosition = filename.lastIndexOf(".");

        if(dotPosition < 0) { //no extension
            return null;
        }

        return filename.substring(dotPosition + 1).toLowerCase(); //return everything after the dot.
    }

    //check List of valid extensions
    private Boolean isExtensionValid(String extension) {

        String testExtension = extension.toLowerCase();
        //iterate over List of valid extensions, split Elements by ","
        for(String validExtension: validExtensions.split(",")) {
            if(testExtension.equals(validExtension)) { //extensions match:
                return true;
            }
        }
        return false; //extensions don't match.
    }

    //create Sub-Dirs for Storing Images - photo001 -
    private File createSubdirectory(String basePath, String prefix) {
        //generate Random Directory Number 0-999:
        int nDirectory = random.nextInt(1000);
        //create Directory Name .format(%String %offsetBy3 Decimal) e.g. prefix009:
        String sDirectory = String.format("%s%03d", prefix, nDirectory);
        //create Directory in baseDirectory, with name of sDirectory:
        File directory = new File(basePath, sDirectory);

        if(!directory.exists()) { //check if directory exists, create directory if doesn't exist:
            directory.mkdir();
        }

        return directory;
    }

    //saving a File with file,base-directory&Prefix
    public FileInfo saveImageFile(MultipartFile file, String baseDirectory,
                                  String subDirPrefix, String filePrefix,
                                  int width, int height) throws InvalidFileException, IOException, ImageTooSmallException {
        //Generate random File-Name:
        int nFileName = random.nextInt(1000);
        String fileName = String.format("%s%03d", filePrefix, nFileName);
        //get Extension:
        String extension = getFileExtension(file.getOriginalFilename());
        //check valid:
        if(extension == null) {
            throw new InvalidFileException("No file extension.");
        }
        if(!isExtensionValid(extension)) {
            throw new InvalidFileException("Not a valid Image file.");
        }
        //create a Subdir:
        File subDirectory = createSubdirectory(baseDirectory, subDirPrefix);

        //Form full Filepath: -CanoncialPath(no dots in pathname!)
        Path filepath = Paths.get(subDirectory.getCanonicalPath(), fileName + "." + extension);

        //Intercept the Image & resize before saving:
        BufferedImage resizedImg = resizeImage(file, width, height); //resizeImg, store in Buffer.

        //write the resized BufferedImage(bufferedImage,formatname,Output-path.toFile):
        ImageIO.write(resizedImg, extension, filepath.toFile());

        //create copy on Binary-Data to created filepath:
//        Files.copy(file.getInputStream(), filepath);

        return new FileInfo(fileName, extension, subDirectory.getName(), baseDirectory);
    }

    private BufferedImage resizeImage(MultipartFile inputFile, int width, int height) throws IOException, ImageTooSmallException {
        //read in Image-Data:
        BufferedImage image = ImageIO.read(inputFile.getInputStream());

        //check if image is too small:
        if(image.getWidth() < width || image.getHeight() < height) {
            throw new ImageTooSmallException();
        }

        //Image scaling - fit shortest dimension, crop rest:
        double widthScale = (double)width/image.getWidth();
        double heightScale = (double)height/image.getHeight();

        double scale = Math.max(widthScale, heightScale); //use whichever value is larger

        BufferedImage scaledImage = new BufferedImage((int)(scale * image.getWidth()),
                (int)(scale * image.getHeight()), image.getType()); //create blank scaled copy of image dimensions

        Graphics2D g2d = scaledImage.createGraphics(); //draw on blank image
        AffineTransform transform = AffineTransform.getScaleInstance(scale, scale); //set desired dimensions for image

        g2d.drawImage(image,transform,null); //draw img from original, apply transform, without return info

        //output the drawn buffered Image:
        return scaledImage.getSubimage(0,0, width, height);
    }
}
