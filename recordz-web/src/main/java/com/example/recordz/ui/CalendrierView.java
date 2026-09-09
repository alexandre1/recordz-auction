package com.example.recordz.ui;

import com.example.recordz.model.domain.EvenementCalendrier;
import com.example.recordz.model.domain.Personne;
import com.example.recordz.service.CalendrierService;
import com.example.recordz.service.PersonneService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;



@Route(value = "calendrier", layout = MainLayout.class)
@PageTitle("Calendrier des enchères")
@PermitAll
public class CalendrierView extends VerticalLayout implements BeforeEnterObserver {

    private final PersonneService personneService;

    private final CalendrierService calendrierService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // ✅ Email disponible ici
        OidcUser user = (OidcUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String email = user.getAttribute("email");
        System.out.println("EMAIL CONNECTE : " + email);

        personneService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Personne non trouvée"));

        // ✅ Construire toute l'UI ici
        removeAll();
        setPadding(true);
        setSpacing(false);

        add(buildSecondaryNav());
        add(new Hr());
        buildCalendrier(email);
    }
    public CalendrierView(CalendrierService calendrierService, PersonneService personneServicee) {

        this.personneService = personneServicee;
        this.calendrierService = calendrierService;
    }

    private HorizontalLayout buildSecondaryNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem");
        nav.add(
                navLink("Informations",             "/mon-compte/informations"),
                navLink("Mes encheres",             "/encheres"),
                navLink("Ajouter un article",       "/ajouter-article"),
                navLink("Faire de la publicité",    "/publicite"),
                navLink("Calendrier de mes ventes", "/calendrier")
        );
        return nav;
    }

    private Anchor navLink(String text, String href) {
        Anchor a = new Anchor(href, text);
        a.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem")
                .set("text-decoration", "none");
        return a;
    }


    private void buildCalendrier(String email) {

        FullCalendar calendar = FullCalendarBuilder.create().build();
        calendar.setSizeFull();
        calendar.setLocale(Locale.FRENCH);
        calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);

    // ── Charger les événements depuis la DB ──────────────

        List<EvenementCalendrier> evenements = calendrierService.findArticlesByVendeurAndStatut(email);

        evenements.forEach(evt ->

    {
        Entry entry = new Entry();
        entry.setTitle(evt.getTitre());
        entry.setStart(evt.getDebut());
        entry.setEnd(evt.getFin());
        entry.setColor(evt.getCouleur());
        calendar.getEntryProvider()
                .asInMemory()
                .addEntry(entry);
    });

    // ── Clic sur un événement ────────────────────────────
        calendar.addEntryClickedListener(event ->

    {
        Entry clicked = event.getEntry();
        Notification.show(
                "Enchère : " + clicked.getTitle(),
                3000,
                Notification.Position.BOTTOM_CENTER
        );
    });

    // ── Barre de navigation ──────────────────────────────
    Button prev = new Button(VaadinIcon.ANGLE_LEFT.create(), e -> calendar.previous());
    Button next = new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next());
    Button today = new Button("Aujourd'hui", e -> calendar.today());

    Select<CalendarView> viewSelect = new Select<>();
        viewSelect.setItems(
    CalendarViewImpl.DAY_GRID_MONTH,
    CalendarViewImpl.TIME_GRID_WEEK,
    CalendarViewImpl.LIST_WEEK
        );
        viewSelect.setValue(CalendarViewImpl.DAY_GRID_MONTH);
        viewSelect.addValueChangeListener(e ->
            calendar.changeView(e.getValue())
            );

    HorizontalLayout toolbar = new HorizontalLayout(
            prev, today, next, viewSelect
    );
        toolbar.setAlignItems(Alignment.CENTER);
    add(toolbar, calendar);
    setSizeFull();
    expand(calendar);
    }
}