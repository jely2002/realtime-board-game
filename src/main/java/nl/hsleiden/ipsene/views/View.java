package nl.hsleiden.ipsene.views;

import nl.hsleiden.ipsene.models.Model;

import java.io.FileNotFoundException;

public interface View {
  public void update(Model m) throws FileNotFoundException;

}
