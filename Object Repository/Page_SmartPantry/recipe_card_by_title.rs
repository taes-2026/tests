<?xml version="1.0" encoding="UTF-8"?>
<WebElementEntity>
   <description>Recipe card filtered by its title. Use the ${title} parameter with findTestObject.</description>
   <name>recipe_card_by_title</name>
   <tag></tag>
   <elementGuidId>4528819e-c1ec-4b49-8c2b-b51768fe257b</elementGuidId>
   <selectorCollection>
      <entry>
         <key>XPATH</key>
         <value>//article[contains(concat(' ', normalize-space(@class), ' '), ' recipe-card ') and .//h3[normalize-space() = &apos;${title}&apos;]]</value>
      </entry>
      <entry>
         <key>CSS</key>
         <value>article.recipe-card</value>
      </entry>
   </selectorCollection>
   <selectorMethod>XPATH</selectorMethod>
   <smartLocatorEnabled>false</smartLocatorEnabled>
   <useRalativeImagePath>true</useRalativeImagePath>
   <webElementProperties>
      <isSelected>false</isSelected>
      <matchCondition>equals</matchCondition>
      <name>tag</name>
      <type>Main</type>
      <value>article</value>
      <webElementGuid>3d4e5f60-7182-4d9e-1f2a-3b4c5d6e7f80</webElementGuid>
   </webElementProperties>
   <webElementProperties>
      <isSelected>true</isSelected>
      <matchCondition>equals</matchCondition>
      <name>text</name>
      <type>Main</type>
      <value>${title}</value>
      <webElementGuid>4e5f6071-8293-4e0f-2a3b-4c5d6e7f8091</webElementGuid>
   </webElementProperties>
   <webElementXpaths>
      <isSelected>false</isSelected>
      <matchCondition>equals</matchCondition>
      <name>xpath:attributes</name>
      <type>Main</type>
      <value>//article[contains(concat(' ', normalize-space(@class), ' '), ' recipe-card ') and .//h3[normalize-space() = &apos;${title}&apos;]]</value>
      <webElementGuid>5f607182-93a4-4f10-3b4c-5d6e7f8091a2</webElementGuid>
   </webElementXpaths>
</WebElementEntity>
