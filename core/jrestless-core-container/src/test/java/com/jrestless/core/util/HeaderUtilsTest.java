package com.jrestless.core.util;

import static com.jrestless.core.util.HeaderUtils.expandHeaders;
import static com.jrestless.core.util.HeaderUtils.flattenHeaders;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;
import com.jrestless.test.UtilityClassCodeCoverageBumper;

public class HeaderUtilsTest {

	@Test
	public void flattenHeaders_NoHeadersGiven_ShouldExpandToEmptyMap() {
		Map<String, List<String>> listHeaders = new HashMap<>();
		Map<String, String> flattenedHeaders = flattenHeaders(listHeaders);
		assertEquals(ImmutableMap.of(), flattenedHeaders);
	}

	@Test
	public void flattenHeaders_NullKeyGiven_ShouldFilterOutHeader() {
		Map<String, List<String>> listHeaders = new HashMap<>();
		listHeaders.put("a_k", singletonList("a_v"));
		listHeaders.put(null, singletonList("b_v"));
		listHeaders.put("c_k", singletonList("c_v"));
		Map<String, String> flattenedHeaders = flattenHeaders(listHeaders);
		assertEquals(ImmutableMap.of("a_k", "a_v", "c_k", "c_v"), flattenedHeaders);
	}

	@Test
	public void flattenHeaders_SingleNullValueGiven_ShouldFilterOutHeader() {
		Map<String, List<String>> listHeaders = new HashMap<>();
		listHeaders.put("a_k", singletonList("a_v"));
		listHeaders.put("b_k", null);
		listHeaders.put("c_k", singletonList("c_v"));
		Map<String, String> flattenedHeaders = flattenHeaders(listHeaders);
		assertEquals(ImmutableMap.of("a_k", "a_v", "c_k", "c_v"), flattenedHeaders);
	}

	@Test
	public void flattenHeaders_EmptyValueListGiven_ShouldFilterOutHeader() {
		Map<String, List<String>> listHeaders = new HashMap<>();
		listHeaders.put("a_k", singletonList("a_v"));
		listHeaders.put("b_k", new ArrayList<>());
		listHeaders.put("c_k", singletonList("c_v"));
		Map<String, String> flattenedHeaders = flattenHeaders(listHeaders);
		assertEquals(ImmutableMap.of("a_k", "a_v", "c_k", "c_v"), flattenedHeaders);
	}

	@Test
	public void flattenHeader_headerNameFilterGiven_ShouldFilterOutHeaders() {
		Map<String, List<String>> listHeaders = new HashMap<>();
		listHeaders.put("a_k", singletonList("a_v"));
		listHeaders.put("b_k", singletonList("b_v"));
		listHeaders.put("c_k", singletonList("c_v"));
		listHeaders.put("d_k", singletonList("d_v"));
		Map<String, String> flattenedHeaders = flattenHeaders(listHeaders, headerName -> !"b_k".equals(headerName) && !"c_k".equals(headerName));
		assertEquals(ImmutableMap.of("a_k", "a_v", "d_k", "d_v"), flattenedHeaders);
	}

	@Test
	public void flattenHeaders_AnyGiven_ShouldReturnImmutableMap() {
		assertThrows(UnsupportedOperationException.class, () -> flattenHeaders(new HashMap<>()).put("k", "v"));
	}

	@Test
	public void flattenHeaders_SingleValueGiven_ShouldPassHeader() {
		Map<String, List<String>> listHeaders = new HashMap<>();
		listHeaders.put("a_k", singletonList("a_v"));
		Map<String, String> flattenedHeaders = flattenHeaders(listHeaders);
		assertEquals(ImmutableMap.of("a_k", "a_v"), flattenedHeaders);
	}

	@Test
	public void flattenHeaders_MultipleValuesGiven_ShouldPassHeader() {
		Map<String, List<String>> listHeaders = new HashMap<>();
		List<String> headerValues = new ArrayList<>();
		headerValues.add("a_v0");
		headerValues.add("a_v1");
		listHeaders.put("a_k", headerValues);
		Map<String, String> flattenedHeaders = flattenHeaders(listHeaders);
		assertEquals(ImmutableMap.of("a_k", "a_v0,a_v1"), flattenedHeaders);
	}

	@Test
	public void expandHeaders_NullKeyGiven_ShouldFilterOutHeader() {
		Map<String, String> flatHeaders = new HashMap<>();
		flatHeaders.put("a_k", "a_v");
		flatHeaders.put(null, "b_v");
		flatHeaders.put("c_k", "c_v");
		Map<String, List<String>> expandedHeaders = expandHeaders(flatHeaders);
		assertEquals(ImmutableMap.of("a_k", singletonList("a_v"), "c_k", singletonList("c_v")), expandedHeaders);
	}

	@Test
	public void expandHeaders_NoHeadersGiven_ShouldExpandToEmptyMap() {
		Map<String, String> flatHeaders = new HashMap<>();
		Map<String, List<String>> expandedHeaders = expandHeaders(flatHeaders);
		assertEquals(ImmutableMap.of(), expandedHeaders);
	}

	@Test
	public void expandHeaders_NullValueGiven_ShouldFilterOutHeader() {
		Map<String, String> flatHeaders = new HashMap<>();
		flatHeaders.put("a_k", "a_v");
		flatHeaders.put("b_k", null);
		flatHeaders.put("c_k", "c_v");
		Map<String, List<String>> expandedHeaders = expandHeaders(flatHeaders);
		assertEquals(ImmutableMap.of("a_k", singletonList("a_v"), "c_k", singletonList("c_v")), expandedHeaders);
	}

	@Test
	public void expandHeaders_CommaSeparatedValuesGiven_ShouldNotSpread() {
		Map<String, String> flatHeaders = new HashMap<>();
		flatHeaders.put("a_k", "a_v0,a_v1");
		Map<String, List<String>> expandedHeaders = expandHeaders(flatHeaders);
		assertEquals(ImmutableMap.of("a_k", singletonList("a_v0,a_v1")), expandedHeaders);
	}

	@Test
	public void exandHeaders_AnyGiven_ShouldReturnImmutableMap() {
		assertThrows(UnsupportedOperationException.class, () -> expandHeaders(new HashMap<>()).put("k", new ArrayList<>()));
	}

	@Test
	public void exandHeaders_AnyGiven_ShouldReturnImmutableList() {
		assertThrows(UnsupportedOperationException.class, () -> expandHeaders(ImmutableMap.of("k", "v0")).get("k").add("v1"));
	}

	@Test
	public void bumpCodeCoverageByInvokingThePrivateConstructor() {
		 UtilityClassCodeCoverageBumper.invokePrivateConstructor(HeaderUtils.class);
	}
}
