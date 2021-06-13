import javafx.stage.Stage;
import nl.hsleiden.ipsene.application.Main;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.hamcrest.CoreMatchers.*;

/**
 * Tests for the Main class
 * @author Loek Appel
 */
@ExtendWith(ApplicationExtension.class)
public class MainTest {

    private Stage stage;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        new Main().start(stage);
    }

    @Test
    @DisplayName("Start should open a window, the right size, with a scene loaded")
    public void testStart() {
        FxAssert.verifyThat(stage.getScene(), notNullValue());
        FxAssert.verifyThat(stage.getHeight(), equalTo(900.0));
        FxAssert.verifyThat(stage.getWidth(), equalTo(1600.0));
    }

    @Test
    @DisplayName("The created window should not be resizable")
    public void testStartNotResizable() {
        FxAssert.verifyThat(stage.resizableProperty().getValue(), is(false));
    }

}
