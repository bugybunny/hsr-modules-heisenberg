/*
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.hsr.modules.uint1.heisenberglibrary.view.model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.model.IModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;

/**
 * @author msyfrig
 */
public class AvailableCopiesComboBoxModel extends AbstractListModel<Copy>
        implements ComboBoxModel<Copy>, IDisposable, Observer {
    private static final long serialVersionUID = -622895063493471778L;
    private List<Copy>        data;
    private Library           library;
    private Copy              selectedCopy;

    public AvailableCopiesComboBoxModel(Library aLibrary) {
        library = aLibrary;
        data = aLibrary.getAvailableCopies();
        library.addObserver(this);
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public Copy getElementAt(int anIndex) {
        return data.get(anIndex);
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if ((selectedCopy != null && anObject instanceof Copy)
                || selectedCopy == null && anObject != null) {
            selectedCopy = (Copy) anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedCopy;
    }

    @Override
    public void cleanUpBeforeDispose() {
        library.deleteObserver(this);
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            IModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Loan.ACTIVE_NUMBER
                    || type == ModelChangeTypeEnums.Copy.NUMBER) {
                data = library.getAvailableCopies();
                int indexOfOldSelectedCopy = data.indexOf(selectedCopy);
                // this is needed to update the gui element for the currently
                // selected item
                if (indexOfOldSelectedCopy >= 0) {
                    setSelectedItem(getElementAt(indexOfOldSelectedCopy));
                } else {
                    setSelectedItem(getElementAt(0));
                }
            }
        }
    }
}
