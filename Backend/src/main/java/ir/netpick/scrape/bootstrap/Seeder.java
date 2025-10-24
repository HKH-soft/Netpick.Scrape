package ir.netpick.scrape.bootstrap;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ir.netpick.scrape.enums.RoleEnum;
import ir.netpick.scrape.models.AuthenticationSignupRequest;
import ir.netpick.scrape.models.Role;
import ir.netpick.scrape.models.User;
import ir.netpick.scrape.repositories.RoleRepository;
import ir.netpick.scrape.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements ApplicationListener<ContextRefreshedEvent> {
  private static final Logger logger = LogManager.getLogger(Seeder.class);

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  // private final ScrapeService scrapeService;
  // private final Scrape scrape;

  private final PasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    this.loadRoles();
    this.createSuperAdmin();
    // this.webScrape();
  }

  private void createSuperAdmin() {
    AuthenticationSignupRequest request = new AuthenticationSignupRequest(
        "super.admin@netpick.ir",
        "password",
        "superAdmin");
    Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
    Optional<User> optionalUser = userRepository.findByEmail(request.email());

    if (optionalRole.isEmpty() || optionalUser.isPresent()) {
      return;
    }

    User user = new User(request.email(), passwordEncoder.encode(request.password()), request.name(),
        optionalRole.get());

    userRepository.save(user);
    logger.info("superuser was created");
  }

  private void loadRoles() {
    RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN };
    Map<RoleEnum, String> roleDescriptionMap = Map.of(
        RoleEnum.USER, "Default user role",
        RoleEnum.ADMIN, "Administrator role",
        RoleEnum.SUPER_ADMIN, "Super Administrator role");

    Arrays.stream(roleNames).forEach((roleName) -> {
      Optional<Role> optionalRole = roleRepository.findByName(roleName);

      optionalRole.ifPresentOrElse(System.out::println, () -> {
        Role roleToCreate = new Role();

        roleToCreate.setName(roleName);
        roleToCreate.setDescription(roleDescriptionMap.get(roleName));

        roleRepository.save(roleToCreate);
      });

    });
  }

  // private void webScrape() {
  // if (!scrapeService.scrapeJobExists("https://netpick.ir")) {
  // ScrapeJob job = new ScrapeJob("https://netpick.ir", "test");
  // scrapeService.createScrapeJob(job);
  // }
  // if
  // (!scrapeService.scrapeJobExists("https://en.wikipedia.org/wiki/Main_Page")) {
  // ScrapeJob job = new ScrapeJob("https://en.wikipedia.org/wiki/Main_Page",
  // "test");
  // scrapeService.createScrapeJob(job);
  // }
  // logger.info(scrape.getDataCount());
  // scrape.webGet(); // yeahhhhhh it workssssss
  // }
}