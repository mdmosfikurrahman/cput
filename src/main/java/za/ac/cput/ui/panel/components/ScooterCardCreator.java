package za.ac.cput.ui.panel.components;

import za.ac.cput.domain.Scooter;

@FunctionalInterface
public interface ScooterCardCreator {
    ScooterCard createCard(Scooter scooter);
}
