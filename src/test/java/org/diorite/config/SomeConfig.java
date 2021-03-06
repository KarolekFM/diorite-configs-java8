/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017. Diorite (by Bartłomiej Mazur (aka GotoFinal))
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.diorite.config;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;

import org.diorite.config.annotations.AsList;
import org.diorite.config.annotations.Comment;
import org.diorite.config.annotations.GroovyValidator;
import org.diorite.config.annotations.HelperMethod;
import org.diorite.config.annotations.Mapped;
import org.diorite.config.annotations.ToStringMapperFunction;
import org.diorite.config.annotations.Unmodifiable;
import org.diorite.config.annotations.Validator;
import org.diorite.config.serialization.EntityStorage;
import org.diorite.config.serialization.MetaObject;

public interface SomeConfig extends Config
{
    @Comment("Nicknames of some weird people.")
    @Property
    @GroovyValidator(isTrue = "!x.contains('Cancer')", elseThrow = "Cancers are not allowed in ${cfg.name()}")
    default List<? extends String> nicknames()
    {
        return new ArrayList<>(Arrays.asList("GotoFinal", "NorthPL"));
    }

    boolean isEqualsToNicknames(Collection<? extends String> strings);

    int nicknamesSize();
    int sizeOfNicknames();
    boolean isNicknamesEmpty();

    @HelperMethod // annotation needed as method matches get<property> pattern
    default String getSomething()
    {
        this.metadata().putIfAbsent("meta", "meta value");
        return "1";
    }

    String getFromNicknames(int index);
    String getNicknamesBy(int index);

    void addToNicknames(String name);
    void putInNicknames(String... names);

    boolean isInNicknames(String name);
    boolean containsNicknames(String... names);
    boolean containsInNicknames(String... names);
    boolean excludesInNicknames(String... names);
    boolean notContainsNicknames(String... names);

    boolean removeFromNicknames(String name);
    boolean removeFromNicknames(String... names);
    boolean removeFromNicknamesIf(Predicate<String> predicate);
    boolean removeFromNicknamesIfNot(Predicate<String> predicate);

    default Map<String, List<String>> getMessages()
    {
        return ImmutableMap.of("first-key", Arrays.asList("first-value", "second-value"));
    }

    EntityStorage getStorage();
    void setStorage(EntityStorage storage);

    @Nullable
    @Validator // second way to create validators, here you can modify result too.
    default EntityStorage storageValidator(@Nullable EntityStorage storage)
    {
        if (storage == null)
        {
            return null;
        }
        if (storage.getEntityData().size() > 100)
        {
            throw new RuntimeException("Too big");
        }
        return storage;
    }

    default String someCustomMethod()
    {
        return this.getStorage().toString();
    }

    @Unmodifiable
    @Comment("Just some special comment!")
    @Mapped
    @ToStringMapperFunction("x.name")
        //-> simpler version
    Collection<? extends MetaObject> getSpecialData();
    void putInSpecialData(MetaObject metaObject);
    boolean removeFromSpecialData(MetaObject metaObject);

    @Unmodifiable
    @Comment("Just some even more special comment!!")
    @AsList(keyProperty = "uuid")
    Map<UUID, ? extends MetaObject> getEvenMoreSpecialData();
    void putInEvenMoreSpecialData(UUID uuid, MetaObject metaObject);
    MetaObject removeFromEvenMoreSpecialData(UUID uuid);
    boolean removeFromEvenMoreSpecialDataIf(BiPredicate<UUID, MetaObject> predicate);
    boolean removeFromEvenMoreSpecialDataIf(Predicate<Entry<UUID, MetaObject>> predicate);

//    @ToStringMapperFunction(property = "specialData")
//    static String metaToKey(MetaObject metaObject)
//    {
//        return metaObject.getName();
//    }

    default TestEnum getEnumValue() {return TestEnum.A;}

    void setEnumValue(TestEnum testEnum);

    enum TestEnum
    {
        A,
        B,
        C
    }
}
