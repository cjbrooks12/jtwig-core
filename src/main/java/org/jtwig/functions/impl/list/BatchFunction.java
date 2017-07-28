package org.jtwig.functions.impl.list;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.SimpleJtwigFunction;
import org.jtwig.util.FunctionValueUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.jtwig.util.FunctionValueUtils.getNumber;

/**
 * The batch filter "batches" items by returning a list of lists with the given number of items. A second parameter can
 * be provided and used to fill in missing items.
 *
 * @functionName batch
 * @compatibilityUrl https://twig.symfony.com/doc/2.x/filters/batch.html
 */
public class BatchFunction extends SimpleJtwigFunction {

    @Override
    public String name() {
        return "batch";
    }

    @Override
    public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(2).maximumNumberOfArguments(3);

        int groupSize = getNumber(request, 1).intValue();

        if (request.getNumberOfArguments() == 3) {
            return batch(request, 0, groupSize, request.get(2));
        } else {
            return batch(request, 0, groupSize);
        }
    }

    private List<List<Object>> batch(FunctionRequest request, int index, int groupSize) {
        Iterator<Object> iterator = FunctionValueUtils.getCollection(request, index).iterator();
        List<List<Object>> result = new ArrayList<>();
        while (iterator.hasNext()) {
            List<Object> batch = new ArrayList<>();
            for (int i = 0; i < groupSize; i++) {
                if (iterator.hasNext())
                    batch.add(iterator.next());
            }
            result.add(batch);
        }
        return result;
    }

    public List<List<Object>> batch(FunctionRequest request, int index, int groupSize, Object padding) {
        Iterator<Object> iterator = FunctionValueUtils.getCollection(request, index).iterator();
        List<List<Object>> result = new ArrayList<>();
        while (iterator.hasNext()) {
            List<Object> batch = new ArrayList<>();
            for (int i = 0; i < groupSize; i++) {
                if (iterator.hasNext())
                    batch.add(iterator.next());
                else
                    batch.add(padding);
            }
            result.add(batch);
        }
        return result;
    }

    /**
     * We'll use the 'apply' function to actually run the final function body. In addition to providing the structure
     * for generating documentation, it also gives us more type-safe function calls.
     *
     * @param source the source object to break into batches
     * @param groupSize the size of each batch
     * @return a list of lists of size `groupSize`
     */
    public List<List<Object>> apply(List<Object> source, int groupSize) {
        return null;
    }

    /**
     * @param source the source object to break into batches
     * @param groupSize the size of each batch
     * @param padding the object used to fill in missing places in the final batch
     * @return a list of lists of size `groupSize`
     */
    public List<List<Object>> apply(List<Object> source, int groupSize, Object padding) {
        return null;
    }
}
