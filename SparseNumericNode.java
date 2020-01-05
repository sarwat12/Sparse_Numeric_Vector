
/**
 * A list node that contains a sparse numeric element.
 *
 */
public class SparseNumericNode {
    private SparseNumericElement element;
    private SparseNumericNode next;

    public SparseNumericNode(SparseNumericElement e, SparseNumericNode n) {
        element = e;
        next = n;
    }
    public SparseNumericElement getElement() { return element;};
    public SparseNumericNode getNext() { return next;};
    public void setNext(SparseNumericNode n) { next = n;};
}

