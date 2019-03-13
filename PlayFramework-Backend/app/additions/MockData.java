package additions;

import models.Category;

public class MockData {
	
	public void generate() {
			
		Category category, childCategory;
		
		//Categories
		String parentCategoryName [] = {"Desktop", "Laptop", "Mobile", "Peripherals", "Parts"};
		String childCategoryName [][] = {
				{"Gaming", "Work"}, 
				{"Gaming", "Work"}, 
				{"Mobile Phones", "Tablets", "Other"},
				{"Keyboards", "Mice", "HeadPhones", "Microphones", "Speakers", "Cameras"},	
				{"Monitors", "Graphic Cards", "Processors", "Motherboards", "Hard Drives", "SSD", "RAM", "PSU", "Printers", "Other"}
			
		};
			
		for(int i = 0; i < parentCategoryName.length; i++) {
			
			category = new Category(parentCategoryName[i], null, null);
			category.save();
			
			for(String cat : childCategoryName[i]) {
				childCategory = new Category(cat, category.id, null);
				childCategory.save();
			}
		}
		
	}

}