
package services;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.FixUpTask;
import domain.Phase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class PhaseServiceTest extends AbstractTest {

	// Service under test -----------------------------------------------------

	@Autowired
	private PhaseService		phaseService;

	// Supporting services ----------------------------------------------------

	@Autowired
	private FixUpTaskService	fixUpTaskService;


	// Test -------------------------------------------------------------------

	@Test
	public void testUpdatePhasePositive() {
		Phase phase;

		phase = this.phaseService.findOne(super.getEntityId("phase8"));

		super.authenticate("handyWorker3");

		phase.setTitle("Título modificado");
		phase.setDescription("Esta fase ha sido modificada para un test");
		this.phaseService.save(phase);

		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNotOwnedPhase() {
		Phase phase;

		phase = this.phaseService.findOne(super.getEntityId("phase8"));

		super.authenticate("handyWorker4");

		phase.setTitle("Título modificado");
		phase.setDescription("Esta fase ha sido modificada para un test");
		this.phaseService.save(phase);

		super.unauthenticate();
	}

	@Test
	public void testDeletePhase() {
		Phase phase;
		FixUpTask referedFUT;

		phase = this.phaseService.findOne(super.getEntityId("phase8"));

		super.authenticate("handyWorker3");

		phase.setTitle("Título modificado");
		phase.setDescription("Esta fase ha sido modificada para un test");
		this.phaseService.delete(phase);

		// referedFUT is the FixUpTask which contains phase8
		referedFUT = this.fixUpTaskService.findOne(super.getEntityId("fixUpTask6"));
		Assert.isTrue(!referedFUT.getPhases().contains(phase));

		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNotOwnedPhase() {
		Phase phase;
		FixUpTask referedFUT;

		phase = this.phaseService.findOne(super.getEntityId("phase8"));

		super.authenticate("handyWorker4");

		phase.setTitle("Título modificado");
		phase.setDescription("Esta fase ha sido modificada para un test");
		this.phaseService.delete(phase);

		// referedFUT is the FixUpTask which contains phase8
		referedFUT = this.fixUpTaskService.findOne(super.getEntityId("fixUpTask6"));
		Assert.isTrue(!referedFUT.getPhases().contains(phase));

		super.unauthenticate();
	}

	@Test
	public void testNewPhasePositive() {
		Phase phase, saved;
		int fixUpTaskId;
		FixUpTask fixUpTask;
		LocalDate localDate;

		fixUpTaskId = super.getEntityId("fixUpTask6");
		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);

		localDate = LocalDate.parse("2017-07-29");

		super.authenticate("handyworker3");

		phase = this.phaseService.create();
		phase.setDescription("Esto es una descripción de prueba");
		phase.setStartMoment(localDate.toDate());
		phase.setEndMoment(localDate.plusDays(10).toDate());
		phase.setTitle("Título TEST");

		saved = this.phaseService.save(fixUpTask, phase);

		super.unauthenticate();

		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		Assert.isTrue(fixUpTask.getPhases().contains(saved));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewPhaseNegative() {
		Phase phase;
		int fixUpTaskId;
		FixUpTask fixUpTask;
		LocalDate localDate;

		fixUpTaskId = super.getEntityId("fixUpTask6");
		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		localDate = LocalDate.parse("2017-07-29");

		super.authenticate("handyworker2");

		phase = this.phaseService.create();
		phase.setDescription("Esto es una descripción de prueba");
		phase.setStartMoment(localDate.toDate());
		phase.setEndMoment(localDate.plusDays(10).toDate());
		phase.setTitle("Título TEST");

		this.phaseService.save(fixUpTask, phase);

		super.unauthenticate();

		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		Assert.isTrue(fixUpTask.getPhases().contains(phase));
	}

	// Phase::startMoment is before than FixUpTask::startMoment
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateConflictDate() {
		Phase phase;

		phase = this.phaseService.findOne(super.getEntityId("phase8"));

		super.authenticate("handyWorker3");

		phase.setTitle("Título modificado");
		phase.setDescription("Esta fase ha sido modificada para un test");
		phase.setStartMoment(LocalDate.parse("2017-07-27").toDate());
		this.phaseService.save(phase);

		super.unauthenticate();
	}

	// Phase::endMoment is after than FixUpTask::endDate
	@Test(expected = IllegalArgumentException.class)
	public void testNewPhaseConflictDate() {
		Phase phase;
		int fixUpTaskId;
		FixUpTask fixUpTask;
		LocalDate localDate;

		fixUpTaskId = super.getEntityId("fixUpTask6");
		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		localDate = LocalDate.parse("2017-07-29");

		super.authenticate("handyworker3");

		phase = this.phaseService.create();
		phase.setDescription("Esto es una descripción de prueba");
		phase.setStartMoment(localDate.toDate());
		phase.setEndMoment(localDate.plusMonths(5).toDate());
		phase.setTitle("Título TEST");

		this.phaseService.save(fixUpTask, phase);

		super.unauthenticate();

		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		Assert.isTrue(fixUpTask.getPhases().contains(phase));
	}

	// EndDate is before than StartDate
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWrongDates() {
		Phase phase;

		phase = this.phaseService.findOne(super.getEntityId("phase8"));

		super.authenticate("handyWorker3");

		phase.setTitle("Título modificado");
		phase.setDescription("Esta fase ha sido modificada para un test");
		phase.setStartMoment(LocalDate.parse("2017-08-02").toDate());
		phase.setEndMoment(LocalDate.parse("2017-07-31").toDate());
		this.phaseService.save(phase);

		super.unauthenticate();
	}
}
