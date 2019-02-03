
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UtilityServiceTest extends AbstractTest {

	// Service under test -----------------------------------------------------

	@Autowired
	private UtilityService	utilityService;


	// Test -------------------------------------------------------------------

	@Test
	public void testCheckCorrectAttachment() {
		String attachment;

		attachment = "http://www.attachment.com";

		this.utilityService.checkAttachments(attachment);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckIncorrectAttachment() {
		String attachment;

		attachment = "zsfgbsdbf";

		this.utilityService.checkAttachments(attachment);
	}

	@Test
	public void testCheckMultipleCorrectAttachment() {
		String attachment;

		attachment = "http://www.attachment.com\rhttp://www.attachment2.com\rhttp://www.attachment3.com";

		this.utilityService.checkAttachments(attachment);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckMultipleIncorrectAttachment() {
		String attachment;

		attachment = "www.attachment.com\rwww.attachment2.com\rwww.attachment3.com";

		this.utilityService.checkAttachments(attachment);
	}
}
