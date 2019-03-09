package additions;

public class PictureFilter {
	
	private static final int maxSize = 2097152;
	private static final int minSize = 4096;

	public boolean isValidPicture(String name, String type, Integer size) {
		if((maxSize >= size && size >= minSize) && type.contains("image") && !(name.isEmpty())){
			return true;
			
		} else {
			return false;
		}
		
	}
	
}
