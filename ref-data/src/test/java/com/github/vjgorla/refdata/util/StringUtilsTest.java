package com.github.vjgorla.refdata.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Map;

import org.junit.Test;

import com.github.vjgorla.refdata.util.StringUtils;

/**
 * 
 * @author Vijaya Gorla
 */
public class StringUtilsTest {

    @Test
    public void givenNullString_whenConvertedToMap_thenMapisEmpty() {
        Map<String, String> map = StringUtils.convertToMap(null, null, null);
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenZeroLengthString_whenConvertedToMap_thenMapisEmpty() {
        Map<String, String> map = StringUtils.convertToMap("", null, null);
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenEmptyString_whenConvertedToMap_thenMapisEmpty() {
        Map<String, String> map = StringUtils.convertToMap("  ", null, null);
        assertThat(map.size(), is(0));
    }

    @Test
    public void givenSingleKeyValueInString_whenConvertedToMap_thenMapHasSingleValue() {
        Map<String, String> map = StringUtils.convertToMap("key=value", ";", "=");
        assertThat(map.size(), is(1));
        assertThat(map.get("key"), is("value"));
    }

    @Test
    public void givenMultipleKeyValuesInString_whenConvertedToMap_thenMapHasMultipleValues() {
        Map<String, String> map = StringUtils.convertToMap("key1=value1;key2=value2", ";", "=");
        assertThat(map.size(), is(2));
        assertThat(map.get("key1"), is("value1"));
        assertThat(map.get("key2"), is("value2"));
    }

    @Test
    public void givenSpacesInString_whenConvertedToMap_thenSpacesAreTrimmed() {
        Map<String, String> map = StringUtils.convertToMap("  key1 = value1  ;  key2=value2 ", ";", "=");
        assertThat(map.size(), is(2));
        assertThat(map.get("key1"), is("value1"));
        assertThat(map.get("key2"), is("value2"));
    }
}
