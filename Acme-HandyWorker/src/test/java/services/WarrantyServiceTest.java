
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Warranty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class WarrantyServiceTest extends AbstractTest {

	// Service under test -----------------------------------------------------

	@Autowired
	private WarrantyService	warrantyService;


	// Test -------------------------------------------------------------------

	@Test
	public void testSaveDeleteWarranty() {
		Warranty warranty, saved;
		Collection<Warranty> warranties;

		super.authenticate("admin1");

		warranty = this.warrantyService.create();
		warranty.setFinalMode(false);
		warranty.setLaws("Test laws");
		warranty.setTerms("Test terms");
		warranty.setTitle("Test title");

		saved = this.warrantyService.save(warranty);
		warranties = this.warrantyService.findAll();
		Assert.isTrue(warranties.contains(saved));

		this.warrantyService.delete(saved);
		warranties = this.warrantyService.findAll();
		Assert.isTrue(!warranties.contains(saved));

		super.unauthenticate();
	}
}
