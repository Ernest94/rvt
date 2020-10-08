package nu.educom.rvt.services;

import nu.educom.rvt.models.Theme;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.ThemeRepository;

public class ConceptService {
  private ConceptRepository conceptRepo;
  private ThemeRepository themeRepo;

  public ConceptService() {
    this.conceptRepo = new ConceptRepository();
    this.themeRepo = new ThemeRepository();
  }
  
  public void addTheme(Theme theme) {
    this.themeRepo.create(theme);
  }
  
}
