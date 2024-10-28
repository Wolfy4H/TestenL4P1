package nl.rocnijmegen.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppIntegrationTest {

    private App app;

    @BeforeEach
    public void setUp() {
        app = new App();
    }

    // Integration Test 1: Full Loan Calculation Process Without Partner or Student Debt
    @Test
    void testLoanCalculationProcess_ZonderPartnerZonderStudieschuld() {
        // Stap 1: Postcodecontrole
        assertTrue(app.controleerPostcode("1234"));  // Geldige postcode

        // Stap 2: Bereken het maximale leenbedrag zonder partner en zonder studieschuld
        double inkomen = 3000.0;
        double partnerInkomen = 0.0;
        boolean heeftStudieschuld = false;
        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(153000.0, maximaalLeenbedrag, 0.01);  // Verwachte waarde is 153000 (3000 * 12 * 4.25)

        // Stap 3: Bereken de maandelijkse betalingen voor een rentevaste periode van 30 jaar
        int jaren = 30;
        double rente = 5.0;
        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rente, jaren);
        assertTrue(maandlasten > 0);  // Controleer of maandelijkse betalingen positief zijn
    }

    // Integration Test 2: Full Loan Calculation Process With Partner and Student Debt
    @Test
    void testLoanCalculationProcess_MetPartnerMetStudieschuld() {
        // Stap 1: Postcodecontrole
        assertTrue(app.controleerPostcode("1234"));  // Geldige postcode

        // Stap 2: Bereken het maximale leenbedrag met partner en met studieschuld
        double inkomen = 3000.0;
        double partnerInkomen = 2000.0;
        boolean heeftStudieschuld = true;
        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(191250.0, maximaalLeenbedrag, 0.01);  // Verwachte waarde is 191250 (5000 * 12 * 4.25 * 0.75)

        // Stap 3: Bereken de maandelijkse betalingen voor een rentevaste periode van 30 jaar
        int jaren = 30;
        double rente = app.bepaalRente(jaren);  // Rente wordt gehaald op basis van de gekozen periode
        assertTrue(rente > 0);  // Controleer of de rente geldig is

        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rente, jaren);
        assertTrue(maandlasten > 0);  // Controleer of maandelijkse betalingen positief zijn
    }

    // Integration Test 3: Full Loan Calculation Process With Student Debt Only
    @Test
    void testLoanCalculationProcess_ZonderPartnerMetStudieschuld() {
        // Stap 1: Postcodecontrole
        assertTrue(app.controleerPostcode("1234"));  // Geldige postcode

        // Stap 2: Bereken het maximale leenbedrag zonder partner, met studieschuld
        double inkomen = 3000.0;  // Individueel inkomen
        double partnerInkomen = 0.0;  // Geen partnerinkomen
        boolean heeftStudieschuld = true;  // Individu heeft een studieschuld
        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(114750.0, maximaalLeenbedrag, 0.01);  // Verwachte waarde is 114750 (3000 * 12 * 4.25 * 0.75)

        // Stap 3: Bereken de maandelijkse betalingen voor een rentevaste periode van 30 jaar
        int jaren = 30;
        double rente = app.bepaalRente(jaren);  // Rente wordt gehaald op basis van de gekozen periode
        assertTrue(rente > 0);  // Controleer of de rente geldig is

        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rente, jaren);
        assertTrue(maandlasten > 0);  // Controleer of maandelijkse betalingen positief zijn
    }
}
