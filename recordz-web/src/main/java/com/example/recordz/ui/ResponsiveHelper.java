package com.example.recordz.ui;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.shared.Registration;

import java.util.Set;

/**
 * Utilitaires pour rendre les vues utilisables sur mobile.
 */
public final class ResponsiveHelper {

    /** En dessous de cette largeur (px), on considère l'écran comme "mobile". */
    public static final int MOBILE_BREAKPOINT_PX = 700;

    private static final String GRID_STATE_KEY = "responsive-hide-columns";

    private ResponsiveHelper() {
    }

    // ── Dialog ────────────────────────────────────────────────────────────

    /**
     * Largeur "maxPx" sur grand écran, 95 % de l'écran sur mobile.
     * Remplace dlg.setWidth("380px").
     */
    public static void dialogWidth(Dialog dialog, int maxPx) {
        dialog.setWidth("95vw");
        dialog.setMaxWidth(maxPx + "px");
        dialog.setMaxHeight("90vh");
    }
    // ── Grid ──────────────────────────────────────────────────────────────

    /**
     * Masque sur petit écran les colonnes dont l'en-tête (setHeader("...")) figure
     * dans la liste. Peut être appelé plusieurs fois sur le même grid (par exemple
     * après removeAllColumns() puis ajout de nouvelles colonnes) : les listeners
     * ne sont enregistrés qu'une fois.
     */
    public static void hideColumnsOnMobile(Grid<?> grid, String... headers) {
        GridState state = (GridState) ComponentUtil.getData(grid, GRID_STATE_KEY);
        if (state == null) {
            state = new GridState(grid);
            ComponentUtil.setData(grid, GRID_STATE_KEY, state);
            state.register();
        }
        state.headers = Set.of(headers);
        state.apply();
    }

    private static final class GridState {
        private final Grid<?> grid;
        private Set<String> headers = Set.of();
        private boolean mobile = false;
        private Registration resizeRegistration;

        private GridState(Grid<?> grid) {
            this.grid = grid;
        }

        private void register() {
            grid.addAttachListener(e -> {
                UI ui = e.getUI();
                // Largeur initiale
                ui.getPage().executeJs("return window.innerWidth")
                        .then(Integer.class, width -> update(width));
                // Changements de taille / rotation
                if (resizeRegistration != null) {
                    resizeRegistration.remove();
                }
                resizeRegistration = ui.getPage()
                        .addBrowserWindowResizeListener(ev -> update(ev.getWidth()));
            });
            grid.addDetachListener(e -> {
                if (resizeRegistration != null) {
                    resizeRegistration.remove();
                    resizeRegistration = null;
                }
            });
        }

        private void update(int width) {
            boolean nowMobile = width < MOBILE_BREAKPOINT_PX;
            if (nowMobile != mobile) {
                mobile = nowMobile;
                apply();
            }
        }

        private void apply() {
            grid.getColumns().stream()
                    .filter(c -> c.getHeaderText() != null && headers.contains(c.getHeaderText()))
                    .forEach(c -> c.setVisible(!mobile));
        }
    }
}
