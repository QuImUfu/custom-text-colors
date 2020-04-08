/*
 * MIT License
 *
 * Copyright (c) 2018-2020 Falkreon (Isaac Ellingson)
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

package quimufu.custom_text_colors.jankson;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import quimufu.custom_text_colors.jankson.api.Marshaller;
import quimufu.custom_text_colors.jankson.impl.MarshallerImpl;
import quimufu.custom_text_colors.jankson.impl.serializer.CommentSerializer;

@SuppressWarnings("deprecation")
public class JsonArray extends quimufu.custom_text_colors.jankson.JsonElement implements List<quimufu.custom_text_colors.jankson.JsonElement>, Iterable<quimufu.custom_text_colors.jankson.JsonElement> {
	private List<Entry> entries = new ArrayList<>();
	protected Marshaller marshaller = MarshallerImpl.getFallback();
	
	public JsonArray() {}
	
	public <T> JsonArray(T[] ts, Marshaller marshaller) {
		this.marshaller = marshaller;
		for(T t : ts) {
			this.add(marshaller.serialize(t));
		}
	}
	
	public JsonArray(Collection<?> ts, Marshaller marshaller) {
		this.marshaller = marshaller;
		for(Object t : ts) {
			this.add(marshaller.serialize(t));
		}
	}
	
	public quimufu.custom_text_colors.jankson.JsonElement get(int i) {
		return entries.get(i).value;
	}
	
	//Convenience getters
	
	public String getString(int index, String defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((quimufu.custom_text_colors.jankson.JsonPrimitive)elem).asString();
		}
		return defaultValue;
	}
	
	public boolean getBoolean(int index, boolean defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((quimufu.custom_text_colors.jankson.JsonPrimitive)elem).asBoolean(defaultValue);
		}
		return defaultValue;
	}
	
	public byte getByte(int index, byte defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((quimufu.custom_text_colors.jankson.JsonPrimitive)elem).asByte(defaultValue);
		}
		return defaultValue;
	}
	
	public char getChar(int index, char defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((quimufu.custom_text_colors.jankson.JsonPrimitive)elem).asChar(defaultValue);
		}
		return defaultValue;
	}
	
	public short getShort(int index, short defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((quimufu.custom_text_colors.jankson.JsonPrimitive)elem).asShort(defaultValue);
		}
		return defaultValue;
	}
	
	public int getInt(int index, int defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((quimufu.custom_text_colors.jankson.JsonPrimitive)elem).asInt(defaultValue);
		}
		return defaultValue;
	}
	
	public long getLong(int index, long defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((quimufu.custom_text_colors.jankson.JsonPrimitive)elem).asLong(defaultValue);
		}
		return defaultValue;
	}
	
	public float getFloat(int index, float defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((quimufu.custom_text_colors.jankson.JsonPrimitive)elem).asFloat(defaultValue);
		}
		return defaultValue;
	}
	
	public double getDouble(int index, double defaultValue) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		if (elem != null && elem instanceof quimufu.custom_text_colors.jankson.JsonPrimitive) {
			return ((JsonPrimitive)elem).asDouble(defaultValue);
		}
		return defaultValue;
	}
	
	public String getComment(int i) {
		return entries.get(i).getComment();
	}
	
	public void setComment(int i, String comment) {
		entries.get(i).setComment(comment);
	}
	
	@Override
	public String toJson(boolean comments, boolean newlines, int depth) {
		quimufu.custom_text_colors.jankson.JsonGrammar grammar = quimufu.custom_text_colors.jankson.JsonGrammar.builder().withComments(comments).printWhitespace(newlines).build();
		return toJson(grammar, depth);
	}
	
	@Override
	public void toJson(Writer writer, JsonGrammar grammar, int depth) throws IOException {
		int effectiveDepth = (grammar.bareRootObject) ? depth-1 : depth;
		//int nextDepth = (grammar.bareRootObject) ? depth-1 : depth; 
		
		writer.append("[");
		
		if (entries.size()>0) {
			if (grammar.printWhitespace) {
				writer.append('\n');
			} else {
				writer.append(' ');
			}
		}
		
		for(int i=0; i<entries.size(); i++) {
			Entry entry = entries.get(i);
			
			if (grammar.printWhitespace) {
				for(int j=0; j<effectiveDepth+1; j++) {
					writer.append("\t");
				}
			}
			
			CommentSerializer.print(writer, entry.getComment(), effectiveDepth, grammar);
			
			writer.append(entry.value.toJson(grammar, depth+1));
			
			if (grammar.printCommas) { 
				if (i<entries.size()-1 || grammar.printTrailingCommas) {
					writer.append(",");
					if (i<entries.size()-1 && !grammar.printWhitespace) writer.append(' ');
				}
			} else {
				writer.append(" ");
			}
			
			if (grammar.printWhitespace) {
				writer.append('\n');
			}
		}
		
		if (entries.size()>0) {
			if (grammar.printWhitespace && depth>0) {
				for(int j=0; j<effectiveDepth; j++) {
					writer.append("\t");
				}
			}
		}
		
		if (entries.size()>0) {
			if (!grammar.printWhitespace) writer.append(' ');
		}
		
		writer.append(']');
	}
	
	public String toString() {
		return toJson(true, false, 0);
	}
	
	public boolean add(@Nonnull quimufu.custom_text_colors.jankson.JsonElement e, String comment) {
		//if (contains(e)) return false;
		
		Entry entry = new Entry();
		entry.value = e;
		entry.setComment(comment);
		entries.add(entry);
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other==null || !(other instanceof JsonArray)) return false;
		
		List<Entry> a = this.entries;
		List<Entry> b = ((JsonArray)other).entries;
		if (a.size()!=b.size()) return false;
		
		for(int i=0; i<a.size(); i++) {
			Entry ae = a.get(i);
			Entry be = b.get(i);
			if (!ae.value.equals(be.value)) return false;
			if (!Objects.equals(ae.getComment(), be.getComment())) return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return entries.hashCode();
	}
	
	@Nullable
	public <E> E get(@Nonnull Class<E> clazz, int index) {
		quimufu.custom_text_colors.jankson.JsonElement elem = get(index);
		return marshaller.marshall(clazz, elem);
	}
	
	
	
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}
	
	public Marshaller getMarshaller() {
		return this.marshaller;
	}
	
	//IMPLEMENTATION for Cloneable
	@Override
	public JsonArray clone() {
		JsonArray result = new JsonArray();
		result.marshaller = marshaller;
		for(Entry entry : entries) {
			result.add(entry.value.clone(), entry.getComment());
		}
		return result;
	}
	
	//implements List<JsonElement> {
	
		@Override
		public int size() {
			return entries.size();
		}
		
		@Override
		public boolean add(@Nonnull quimufu.custom_text_colors.jankson.JsonElement e) {
			Entry entry = new Entry();
			entry.value = e;
			entries.add(entry);
			return true;
		}
		
		@Override
		public boolean addAll(Collection<? extends quimufu.custom_text_colors.jankson.JsonElement> c) {
			boolean result = false;
			for(quimufu.custom_text_colors.jankson.JsonElement elem : c) result |= add(elem);
			
			return result;
		}
		
		@Override
		public void clear() {
			entries.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if (o==null || !(o instanceof quimufu.custom_text_colors.jankson.JsonElement)) return false;
			
			for(Entry entry : entries) {
				if (entry.value.equals(o)) return true;
			}
			return false;
		}
		
		@Override
		public boolean containsAll(Collection<?> c) {
			for(Object o : c) {
				if (!contains(o)) return false;
			}
			
			return true;
		}
		
		@Override
		public boolean isEmpty() {
			return entries.isEmpty();
		}
		
		@Override
		public boolean remove(Object o) {
			for(int i=0; i<entries.size(); i++) {
				Entry cur = entries.get(i);
				if (cur.value.equals(o)) {
					entries.remove(i);
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException("removeAll not supported");
		}
		
		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException("retainAll not supported");
		}
		
		@Override
		public quimufu.custom_text_colors.jankson.JsonElement[] toArray() {
			quimufu.custom_text_colors.jankson.JsonElement[] result = new quimufu.custom_text_colors.jankson.JsonElement[entries.size()];
			for(int i=0; i<entries.size(); i++) {
				result[i] = entries.get(i).value;
			}
			return result;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			if (a.length<entries.size()) a = (T[]) new Object[entries.size()];
			for(int i=0; i<entries.size(); i++) {
				a[i] = (T)entries.get(i).value;
			}
			if (a.length>entries.size()) {
				a[entries.size()] = null; //Little-known and basically unused quirk of the toArray contract
			}
			return a;
		}
		
		@Override
		public Iterator<quimufu.custom_text_colors.jankson.JsonElement> iterator() {
			return new EntryIterator(entries);
		}
	
		@Override
		public void add(int index, quimufu.custom_text_colors.jankson.JsonElement element) {
			entries.add(index, new Entry(element));
		}
	
		@Override
		public boolean addAll(int index, Collection<? extends quimufu.custom_text_colors.jankson.JsonElement> elements) {
			if (elements.isEmpty()) return false;
			int i = index;
			for (quimufu.custom_text_colors.jankson.JsonElement element : elements) {
				entries.add(i, new Entry(element));
				i++;
			}
			return true;
		}
	
		@Override
		public int indexOf(Object obj) {
			if (obj==null) return -1;
			for(int i=0; i<entries.size(); i++) {
				quimufu.custom_text_colors.jankson.JsonElement val = entries.get(i).value;
				if (val!=null && val.equals(obj)) return i;
			}
			return -1;
		}
	
		@Override
		public int lastIndexOf(Object obj) {
			if (obj==null) return -1;
			for(int i=entries.size()-1; i>=0; i--) {
				quimufu.custom_text_colors.jankson.JsonElement val = entries.get(i).value;
				if (val!=null && val.equals(obj)) return i;
			}
			return -1;
		}
	
		@Override
		public ListIterator<quimufu.custom_text_colors.jankson.JsonElement> listIterator() {
			return new EntryIterator(entries);
		}
	
		@Override
		public ListIterator<quimufu.custom_text_colors.jankson.JsonElement> listIterator(int index) {
			return new EntryIterator(entries, index);
		}
	
		@Override
		public quimufu.custom_text_colors.jankson.JsonElement remove(int index) {
			return entries.remove(index).value;
		}
	
		@Override
		public quimufu.custom_text_colors.jankson.JsonElement set(int index, quimufu.custom_text_colors.jankson.JsonElement element) {
			Entry cur = new Entry(element);
			Entry old = entries.get(index);
			if (old!=null) cur.setComment(old.getComment());
			entries.set(index, cur);
			
			return (old==null) ? null : old.value;
		}
	
		@Override
		public List<quimufu.custom_text_colors.jankson.JsonElement> subList(int arg0, int arg1) {
			throw new UnsupportedOperationException(); //TODO: Implement
		}
	
	//}
	
	
	//MISC CLASSES
	
	private static class EntryIterator implements ListIterator<quimufu.custom_text_colors.jankson.JsonElement> {
		private final ListIterator<Entry> delegate;
		
		public EntryIterator(List<Entry> list) {
			delegate = list.listIterator();
		}
		
		public EntryIterator(List<Entry> list, int index) {
			delegate = list.listIterator(index);
		}
		
		@Override
		public boolean hasNext() {
			return delegate.hasNext();
		}

		@Override
		public quimufu.custom_text_colors.jankson.JsonElement next() {
			return delegate.next().value;
		}
		
		@Override
		public void remove() {
			delegate.remove();
		}

		@Override
		public void add(quimufu.custom_text_colors.jankson.JsonElement elem) {
			delegate.add(new Entry(elem));
		}

		@Override
		public boolean hasPrevious() {
			return delegate.hasPrevious();
		}

		@Override
		public int nextIndex() {
			return delegate.nextIndex();
		}

		@Override
		public quimufu.custom_text_colors.jankson.JsonElement previous() {
			return delegate.previous().value;
		}

		@Override
		public int previousIndex() {
			return delegate.previousIndex();
		}

		@Override
		public void set(quimufu.custom_text_colors.jankson.JsonElement obj) {
			delegate.set(new Entry(obj));
		}
	}
	
	private static class Entry {
		String comment;
		quimufu.custom_text_colors.jankson.JsonElement value;
		
		public Entry() {}
		public Entry(JsonElement value) { this.value = value; }
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Entry)) return false;
			Entry o = (Entry)other;
			return Objects.equals(comment, o.comment) &&
					Objects.equals(value, o.value);
		}
		
		public String getComment() {
			return comment;
		}
		
		public void setComment(String comment) {
			if (comment!=null && !comment.trim().isEmpty()) {
				this.comment = comment;
			} else {
				this.comment = null;
			}
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(comment, value);
		}
	}
}
