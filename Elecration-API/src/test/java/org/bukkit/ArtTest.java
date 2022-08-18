package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class ArtTest {

    public void getByNullName() {
        Art.getByName(null);
    }

    public void getById() {
        for (Art art : Art.values()) {
            assertThat(Art.getById(art.getId()), is(art));
        }
    }

    public void getByName() {
        for (Art art : Art.values()) {
            assertThat(Art.getByName(art.toString()), is(art));
        }
    }

    public void dimensionSanityCheck() {
        for (Art art : Art.values()) {
            assertThat(art.getBlockHeight(), is(greaterThan(0)));
            assertThat(art.getBlockWidth(), is(greaterThan(0)));
        }
    }

    public void getByNameWithMixedCase() {
        Art subject = Art.values()[0];
        String name = subject.toString().replace('E', 'e');

        assertThat(Art.getByName(name), is(subject));
    }
}
