/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.modulith.borrow.domain;

import org.jmolecules.architecture.hexagonal.SecondaryPort;

@SecondaryPort
public interface HoldEventPublisher {

    void holdPlaced(BookPlacedOnHold event);

    default Hold holdPlaced(Hold hold) {
        BookPlacedOnHold event =
                new BookPlacedOnHold(
                        hold.getId().id(), hold.getOnBook().barcode(), hold.getDateOfHold());
        this.holdPlaced(event);
        return hold;
    }

    //    void bookCheckedOut(Checkout.BookCheckedOut event);
    //
    //    default Checkout bookCheckedOut(Checkout checkout) {
    //        Checkout.BookCheckedOut event = new Checkout.BookCheckedOut(checkout);
    //        this.bookCheckedOut(event);
    //        return checkout;
    //    }
}
