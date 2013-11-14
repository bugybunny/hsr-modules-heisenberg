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
package ch.hsr.modules.uint1.heisenberglibrary.model;

/**
 * 
 * @author mstolze
 * @author msyfrig
 */
public class Copy extends AbstractObservable {

    public enum Condition {
        NEW, GOOD, DAMAGED, WASTE, LOST
    }

    public static long   nextInventoryNumber = 1;

    private final long   inventoryNumber;
    private final BookDO bookDO;
    private Condition    condition;

    public Copy(BookDO title) {
        this.bookDO = title;
        inventoryNumber = nextInventoryNumber++;
        condition = Condition.NEW;
    }

    public BookDO getTitle() {
        return bookDO;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition aCondition) {
        Condition oldValue = condition;
        condition = aCondition;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Copy.CONDITION, oldValue, condition));
    }

    public long getInventoryNumber() {
        return inventoryNumber;
    }
}
