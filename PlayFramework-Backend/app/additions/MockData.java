package additions;

import com.typesafe.config.ConfigFactory;
import models.Category;
import models.User;

public class MockData {
	
	public void generate() {
		
		//Admin
//		User admin = new User();

		try {
//			admin.name = "admin";
//			admin.surname= "admin";
//			admin.email = (ConfigFactory.load().getString("custom.admin.mail"));
//			admin.setPassword(ConfigFactory.load().getString("custom.admin.password"));
//			admin.admin = true;		
//			admin.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Category category, childCategory;
		
		//Categories
		String parentCategoryName [] = {"Desktop", "Laptop", "Mobile", "Peripherals", "Parts"};
		String childCategoryName [][] = {
				{}, 
				{}, 
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