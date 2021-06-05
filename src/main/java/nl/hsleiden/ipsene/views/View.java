package nl.hsleiden.ipsene.views;

import java.io.FileNotFoundException;
import nl.hsleiden.ipsene.models.Model;

public interface View {
  public void update(Model m) throws FileNotFoundException;
}
