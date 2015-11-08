/*
 * Copyright (C) 2015 Vinaya Prasad N
 *
 *         This program is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package test.com.nvinayshetty.DTOnator.FeedValidator.FieldCreator;

import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldType;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationFactory;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationStrategy;
import com.nvinayshetty.DTOnator.FieldCreator.GsonFieldCreator;
import com.nvinayshetty.DTOnator.FieldCreator.SimpleFieldCreator;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by vinay on 16/8/15.
 */
public class FieldCreationFactoryShould {
    @Test
    public void shouldCretaGsonFieldWhenUserInputIsGson(){
        FieldCreationStrategy fieldCreationStrategy= FieldCreationFactory.getFieldCreatorFor(FieldType.GSON);
        assertTrue(fieldCreationStrategy instanceof GsonFieldCreator);

        FieldCreationStrategy SimpleFieldCreationStrategy= FieldCreationFactory.getFieldCreatorFor(FieldType.POJO);
        assertTrue(SimpleFieldCreationStrategy instanceof SimpleFieldCreator);
    }
}
