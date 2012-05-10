package festivalnauke.rni.napravisvojsajt.model;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.services.IDisposable;

public class Picture extends SiteElement implements IDisposable {
	
	private static final int WIDTH = 100;
	
	protected Image picture;
	protected Image scaledPicture;
	protected String picturePath;
	protected PictureLocation location = PictureLocation.TOPLEFT;
	
	public enum PictureLocation {
		TOPLEFT,
		TOPRIGHT,
		BOTTOMLEFT,
		BOTTOMRIGHT
	}

	public Picture(SiteElement parent, Image picture, String path) {
		super();
		this.parent = parent;
		this.picture = picture;
		this.picturePath = path;
		
		ImageData imageData = picture.getImageData();
		int width = imageData.width;
		int height = imageData.height;
		double ratio = width/WIDTH;
		if(ratio == 0)
			ratio = 1;
		
		this.scaledPicture = new Image(picture.getDevice(), 
				picture.getImageData().scaledTo((int)(width/ratio), (int)(height/ratio)));
		this.parent.addSiteElement(this);
	}
	
	public void dispose(){
		picture.dispose();
	}

	public Image getImage() {
		return this.picture;
	}

	public Image getScalledImage() {
		return scaledPicture;
	}

	public String getPicturePath() {
		return picturePath;
	}
	
	public String getPictureName() {
		return new File(picturePath).getName();
	}
	
}
