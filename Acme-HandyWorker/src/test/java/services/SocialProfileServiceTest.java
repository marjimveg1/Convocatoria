
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	// Service under test ---------------------------------

	@Autowired
	private SocialProfileService	socialProfileService;


	// Tests ----------------------------------------------

	@Test
	public void testCreate() {
		super.authenticate("HandyWorker1");
		SocialProfile socialProfile;
		socialProfile = this.socialProfileService.create();
		Assert.notNull(socialProfile);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		SocialProfile socialProfile;
		socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfile1"));

		Assert.notNull(socialProfile);
	}

	@Test
	public void testSave() {
		super.authenticate("HandyWorker1");
		SocialProfile socialProfile;
		socialProfile = this.socialProfileService.create();

		socialProfile.setProfileLink("http://www.instagram.com");
		socialProfile.setSocialNetworkName("instagram");
		socialProfile.setNick("instagramNick");
		this.socialProfileService.save(socialProfile);
		super.unauthenticate();
	}

	@Test
	public void testSaveEdit() {
		super.authenticate("customer1");
		SocialProfile socialProfile;
		socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfile2"));

		socialProfile.setProfileLink("http://www.instagram.com");
		socialProfile.setSocialNetworkName("instagram");
		socialProfile.setNick("instagramNick");
		this.socialProfileService.save(socialProfile);
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveEditNegative() {
		super.authenticate("customer1");
		SocialProfile socialProfile;
		socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfile3"));

		socialProfile.setProfileLink("http://www.instagram.com");
		socialProfile.setSocialNetworkName("instagram");
		socialProfile.setNick("instagramNick");
		this.socialProfileService.save(socialProfile);
		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("customer1");
		SocialProfile socialProfile;
		socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfile2"));
		this.socialProfileService.delete(socialProfile);
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNegative() {
		super.authenticate("customer1");
		SocialProfile socialProfile;
		socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfile3"));
		this.socialProfileService.delete(socialProfile);
		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		Collection<SocialProfile> socialProfiles;
		socialProfiles = this.socialProfileService.findAll();
		Assert.notNull(socialProfiles);
	}
}
