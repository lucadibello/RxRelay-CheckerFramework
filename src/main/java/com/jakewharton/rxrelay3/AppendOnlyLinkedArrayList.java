/**
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.jakewharton.rxrelay3;

import io.reactivex.rxjava3.functions.Predicate;
import org.checkerframework.checker.index.qual.*;
import org.checkerframework.common.value.qual.MinLen;

/**
 * A linked-array-list implementation that only supports appending and consumption.
 *
 * @param <T> the value type
 */
class AppendOnlyLinkedArrayList<T> {

    private final @NonNegative @LTLengthOf({"head", "tail"}) int capacity;
    private final @MinLen(1) Object[] head; // Reference to first object of array
    private @MinLen(1) Object[] tail; // Reference to the last object of the array

    // Position of the first empty spot in the array (by construction, could be at most equal to capacity)
    private @NonNegative @LTLengthOf("tail") int offset;

    /**
     * Constructs an empty list with a per-link capacity.
     * @param capacity the capacity of each link
     */
    AppendOnlyLinkedArrayList(@NonNegative int capacity) {
        this.head = new Object[capacity + 1];
        this.tail = head;
        this.capacity = capacity;
    }

    /**
     * Append a non-null value to the list.
     * <p>Don't add null to the list!
     * @param value the value to append
     */
    @SuppressWarnings("value:assignment") // Check NOTICE!
    void add(T value) {
        // Save locally capacity of the array and the current offset (index to save next element)
        final @NonNegative @LTLengthOf("tail") int c = capacity;
        // Get the index of the last element
        @NonNegative @LTLengthOf("tail") int o = offset;
        // Check if o is higher than the capacity of the array (not a valid index)
        if (o == c) {
            // If the capacity is reached, we create an array of size c+1 (we double the size)
            @MinLen(1) Object[] next = new Object[c + 1];

            // We assign this array as the last element of the linked list
            // NOTICE: This is a trick to avoid using a linked list data structure, but still have a linked list behavior.
            //          unfortunately, CheckerFramework does not like this kind of operation and will throw a warning as
            //          the type of the array cell is Object and not Object[]. For this reason, we have to suppress the
            //          warning.
            tail[c] = next;

            // We move the tail on the new array, meaning we are now working on the newly created array
            // This effectively doubles the size of the linked list!
            tail = next;

            // Reset the offset to 0 in order to use the new array in its entirely
            o = 0;
        }

        // The offset is now guaranteed to be less than the capacity of the array!
        assert o < c: "@AssumeAssertion(index): CheckerFramework is not able to understand that o is always less than c";

        // Save the value at the current offset
        tail[o] = value;
        // Increase the offset to the next slot
        offset = o + 1;
    }

    /**
     * Predicate interface suppressing the exception.
     *
     * @param <T> the value type
     */
    public interface NonThrowingPredicate<T> extends Predicate<T> {
        @Override
        boolean test(T t);
    }

    /**
     * Loops over all elements of the array until a null element is encountered or
     * the given predicate returns true.
     * @param consumer the consumer of values that returns true if the forEach should terminate
     */
    @SuppressWarnings("unchecked")
    void forEachWhile(NonThrowingPredicate<? super T> consumer) {
        Object[] a = head;
        final @NonNegative @LTLengthOf("a") int c = capacity;
        // While the element is not null
        while (a != null) {
            for (@NonNegative int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null) {
                    break;
                }
                if (consumer.test((T) o)) {
                    break;
                }
            }
            // move to next element in array
            a = (Object[]) a[c];
        }
    }

    @SuppressWarnings("unchecked")
    void accept(Relay<? super T> observer) {
        Object[] a = head;
        final @NonNegative @LTLengthOf("a") int c = capacity;
        while (a != null) {
            for (@NonNegative int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null) {
                    break;
                }

                observer.accept((T) o);
            }
            a = (Object[]) a[c];
        }
    }
}
