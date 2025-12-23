package oodj_project.core.ui.components;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

import oodj_project.core.ui.utils.Clickable;
import oodj_project.core.ui.utils.IconManager;

public class IconButton extends JButton {
    public IconButton(String imageResourcePath, int width, int height) {
        this(IconManager.getIcon(imageResourcePath, width, height));
    }

    public IconButton(Icon icon) {
        super(icon);
        addMouseListener(Clickable.INSTANCE);

        setBorder(
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
            // BorderFactory.createCompoundBorder(
            //     BorderFactory.createLineBorder(Color.BLACK),
            //     BorderFactory.createEmptyBorder(2,2,2,2)
            // )
        );

        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }
}

// https://www.svgrepo.com/svg/532551/search-alt-1?edit=true #000000
// https://www.svgrepo.com/svg/524551/filter?edit=true #000000
// https://www.svgrepo.com/svg/527497/sort?edit=true #000000

// https://www.svgrepo.com/svg/528634/sort-from-top-to-bottom?edit=true #000000
// https://www.svgrepo.com/svg/528629/sort-from-bottom-to-top?edit=true #000000

// https://www.svgrepo.com/svg/513803/add?edit=true #000000

// https://www.svgrepo.com/svg/505875/edit-4?edit=true #0abd16
// https://www.svgrepo.com/svg/502608/delete-2?edit=true #ff0000

// https://www.svgrepo.com/svg/448962/chapter-previous-filled?edit=true #000000
// https://www.svgrepo.com/svg/448952/caret-previous-filled?edit=true #000000
// https://www.svgrepo.com/svg/448949/caret-next-filled?edit=true #000000
// https://www.svgrepo.com/svg/448961/chapter-next-filled?edit=true #000000
