package com.coffeecat.springbootcourse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.coffeecat.springbootcourse.model.entity.StatusUpdate;
import com.coffeecat.springbootcourse.model.repository.StatusUpdateDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Calendar;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional //annotate to run the Test in a Transaction!
//Now: TestRuns -> Transaction occurs -> (output) -> ROLLBACK(changes aren't committed to DB!)
public class StatusTest {

    @Autowired //Obj created by Spring automatically, annotation used to refer to Object.
    private StatusUpdateDao statusUpdateDao; //implementation is now automatically provided by Spring.

    @Test
    public void testSave() {
        StatusUpdate status = new StatusUpdate("This is a test Status Update");

        statusUpdateDao.save(status); //save to DB

        assertNotNull(status.getId(), "Non-NULL ID");
        assertNotNull(status.getAdded(), "Non-NULL Date");

        //Retrieve Object:
        StatusUpdate retrieved = statusUpdateDao.findById(status.getId()).get();

        assertEquals(status, retrieved, "Matching StatusUpdate");
    }

    @Test
    public void testFindLatest() {
        // use fake date:
        Calendar calendar = Calendar.getInstance();

        StatusUpdate lastStatusUpdate = null;

        for(int i=0; i<10; i++) {
           calendar.add(Calendar.DAY_OF_YEAR, 1); //increment by  1day each loop
            //create unique Status Update for current Date:
            StatusUpdate status = new StatusUpdate("Status Update" + i, calendar.getTime());

            //save obj:
            statusUpdateDao.save(status);
            lastStatusUpdate = status; // use to save last status update in loop.
        }

        //run check:
        StatusUpdate retrieved = statusUpdateDao.findFirstByOrderByAddedDesc();
        //check if the last status update is the expected value:
        assertEquals(lastStatusUpdate, retrieved,  "Latest status update.");
    }
}
