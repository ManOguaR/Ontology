/*
 *  $Id$
 *
 * Copyright (c) 2013 Doug Palmer
 *
 * See LICENSE for licensing details
 */
package org.charvolant.argushiigi.server;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.charvolant.argushiigi.ontology.Category;
import org.charvolant.argushiigi.ontology.CategoryVisitor;
import org.charvolant.argushiigi.ontology.DisplaySorter;
import org.restlet.data.Language;
import org.restlet.representation.Representation;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * A class that can be used to render a resource in
 * some useful way. Subclasses are responsible for 
 * actual rendering.
 * <p>
 * Renderers are not thread-safe.
 * 
 * @author Doug Palmer <doug@charvolant.org>
 *
 */
abstract public class Renderer implements CategoryVisitor {
  /** The resource being rendered (set during render) */
  protected Resource resource;
  /** The locale to use */
  protected Locale locale;
  /** The display sorter */
  protected DisplaySorter sorter;
  /** The appliction */
  protected ServerApplication application;
  /** The resource comparator */
  protected Comparator<Resource> resourceComparator;
  /** The resource comparator */
  protected Comparator<Statement> statementComparator;
  
  /**
   * Construct a renderer.
   *
   * @param locale The locale to render in
   * @param sorter The sorter to use when ordering elements
   * @param application The server application
   * 
   * @throws Exception if unable to build some sub element
   */
  public Renderer(Locale locale, DisplaySorter sorter, ServerApplication application) throws Exception {
    super();
    this.resource = null;
    this.locale = locale;
    this.sorter = sorter;
    this.application = application;
    this.resourceComparator = this.sorter.getResourceComparator(this.locale);
    this.statementComparator = this.sorter.getStatementComparator(this.locale);
  }
  
  /**
   * Get the language list for this representation.
   * 
   * @return A singleton with the appropriate language 
   */
  public List<Language> getLanguages() {
    return Collections.singletonList(new Language(this.locale.toLanguageTag()));
  }
  
  /**
   * Render a categorisation of a resource.
   * 
   * @param resource The resource
   * @param top The top category
   */
  public void render(Resource resource, Category top) {
    this.resource = resource;
    this.preRender(top);
    top.visit(this);
    this.postRender(top);
  }
 
  /**
   * Perform an pre-rendering activities.
   * 
   * @param top The top category
   */
  abstract protected void preRender(Category top);
  
  /**
   * Perform an post-rendering activities.
   * 
   * @param top The top category
   */
  abstract protected void postRender(Category top);
  
  /**
   * Get the representation of what has been rendered.
   * 
   * @return The representation
   */
  abstract public Representation getRepresentation();
}