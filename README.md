<p align="center">
	<em>
		<img src="https://tryharddood.github.io/custom/projects/izone/img/izone.png" alt="[IMG]" data-url="https://tryharddood.github.io/custom/projects/izone/img/izone.png" class="fr-fic fr-dib"/>
		<span>&nbsp;</span>
		<br/>
		<strong>Zone Protection Made Easy!</strong>
		<br/>
	</em>
	<br/>
	<a href="https://github.com/TryHardDood/iZone/blob/master/README.md#features" rel="nofollow" target="_blank">Features</a> |
		
	<a href="https://github.com/TryHardDood/iZone/blob/master/README.md#installation" rel="nofollow" target="_blank">Installation</a> |
		
	<a href="https://github.com/TryHardDood/iZone/blob/master/README.md#commands" rel="nofollow" target="_blank">Commands</a> |
		
	<a href="https://github.com/TryHardDood/iZone/blob/master/README.md#flags_and_variables" rel="nofollow" target="_blank">Flags and Variables</a> |
		
	<a href="https://github.com/TryHardDood/iZone/blob/master/README.md#configuration" rel="nofollow" target="_blank">Configuration</a>
</p>
<p align="center">
	<a href="https://github.com/TryHardDood/iZone/issues" rel="nofollow" target="_blank">Report an issue</a>
</p>
<p>
	<a href="https://travis-ci.org/TryHardDood/iZone" rel="nofollow" target="_blank"></a>
</p>
<p align="center">
	<strong>ONLY WORKS ON SPIGOT!</strong>
</p>
<p>
	<span>
		<strong>Features</strong>
	</span>
</p>
<ol>
	<li>Zone protection</li>
	<li>Easy-to-use</li>
	<li>Update notifier</li>
	<li>1.7.10, 1.8.X, 1.9.4, 1.11 support.</li>
	<li>Multilanguage support.</li>
	<li>Players can use it</li>
	<li>Useful flags ( like &lsquo;fly&rsquo; or &lsquo;water-flow&rsquo; )</li>
</ol>
<h3>
	<a id="installation">Installation</a>
</h3>
<ol>
	<li>Download
			
		<span>&nbsp;</span>
		<a href="https://www.spigotmc.org/resources/izone.23349/">iZone</a>
	</li>
	<li>Make sure your server is not running.</li>
	<li>Copy the .jar file into your plugins directory.</li>
	<li>Start the server.</li>
	<li>If you made a backup of your config.yml file, stop the server and edit the newly generated config.yml file with only what you need, from the backup.</li>
	<li>Start the server.</li>
	<li>Enjoy!</li>
</ol>
<h3>
	<a id="commands">Commands</a>
</h3>
<table>
	<thead>
		<tr>
			<th>Command
				
					
				<br/>
			</th>
			<th>Details
				
					
				<br/>
			</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>/zmod
				
					
				<br/>
			</td>
			<td>Opens a gui where you can access all of your zones and modify them.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod create [name]
				
					
				<br/>
			</td>
			<td>Creates a zone with the given name.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod delete [name]
				
					
				<br/>
			</td>
			<td>Deletes a zone.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod help
				
					
				<br/>
			</td>
			<td>List of commands in-game.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod list
				
					
				<br/>
			</td>
			<td>Prints list off all owned zones.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod who
				
					
				<br/>
			</td>
			<td>List of players in your current zone.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod info [name]
				
					
				<br/>
			</td>
			<td>Prints info about the zone.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod allow [zone] [player]
				
					
				<br/>
			</td>
			<td>Add player to zone list.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod disallow [zone] [player]
				
					
				<br/>
			</td>
			<td>Remove player to zone list.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod flag [zone] [flag] [flag data]
				
					
				<br/>
			</td>
			<td>Toggle a zone flag
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod parent [child] [parent]
				
					
				<br/>
			</td>
			<td>Set a parent for a child zone.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod expand [zone] [size] [direction]
				
					
				<br/>
			</td>
			<td>Expand the borders up or down.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/zmod visualise
				
					
				<br/>
			</td>
			<td>Shows the selected area&rsquo;s borders for couple seconds.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>/izone [reload]
				
					
				<br/>
			</td>
			<td>Shows the plugin information and reload the current configuration.
				
					
				<br/>
			</td>
		</tr>
	</tbody>
</table>
<p>
	<br/>
</p>
<h3>
	<a id="flags_and_variables">Flags and Variables</a>
</h3>
<table>
	<thead>
		<tr>
			<th>Flag
				
					
				<br/>
			</th>
			<th>Details
				
					
				<br/>
			</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>PROTECTION
				
					
				<br/>
			</td>
			<td>Protects everything
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>MONSTER
				
					
				<br/>
			</td>
			<td>Disables mob spawning
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>ANIMAL
				
					
				<br/>
			</td>
			<td>Disables animal spawning
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>WELCOME
				
					
				<br/>
			</td>
			<td>Welcome message in chat and title
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>FAREWELL
				
					
				<br/>
			</td>
			<td>Farewell message in chat and title
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>HEAL
				
					
				<br/>
			</td>
			<td>Heals you with the given amount when you&rsquo;re in the zone.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>HURT
				
					
				<br/>
			</td>
			<td>Hurts you with the given amount when you&rsquo;re in the zone.
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>PVP
				
					
				<br/>
			</td>
			<td>Disables or Enables the PVP
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>CREEPER
				
					
				<br/>
			</td>
			<td>Disable Creeper explosion damage
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>TNT
				
					
				<br/>
			</td>
			<td>Disable TNT damage
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>EXPLOSION
				
					
				<br/>
			</td>
			<td>Disable other explosions damage
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>FIRE
				
					
				<br/>
			</td>
			<td>Blocks fire spread
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>RESTRICTION
				
					
				<br/>
			</td>
			<td>Restrict the entry
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>JAIL
				
					
				<br/>
			</td>
			<td>Can&#39;t get out of it
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>LIGHTNING
				
					
				<br/>
			</td>
			<td>Disables lightning strikes in the zone
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>DEATHDROP
				
					
				<br/>
			</td>
			<td>No damage on fall
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>SAFEDEATH
				
					
				<br/>
			</td>
			<td>No item or exp loss when you die
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>DROP
				
					
				<br/>
			</td>
			<td>Disables dropping items
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>INTERACT
				
					
				<br/>
			</td>
			<td>Disable/Enable interacting in the zone
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>ENDERMAN
				
					
				<br/>
			</td>
			<td>Disable Enderman&rsquo;s block grief
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>GOD
				
					
				<br/>
			</td>
			<td>Enable/Disable god mode
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>GAMEMODE
				
					
				<br/>
			</td>
			<td>Change the player&rsquo;s gamemode when they&rsquo;re in the zone
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>FLY
				
					
				<br/>
			</td>
			<td>Disable or Enable flying
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>TAKEITEM_IN
				
					
				<br/>
			</td>
			<td>When entering the zone it takes an item
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>TAKEITEM_OUT
				
					
				<br/>
			</td>
			<td>When leaving the zone it takes an item
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>GIVEITEM_IN
				
					
				<br/>
			</td>
			<td>When entering the zone it gives an item
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>GIVEITEM_OUT
				
					
				<br/>
			</td>
			<td>When leaving the zone it gives an item
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>TAKEEFFECT_IN
				
					
				<br/>
			</td>
			<td>When entering the zone it takes an effect
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>TAKEEFFECT_OUT
				
					
				<br/>
			</td>
			<td>When leaving the zone it takes an effect
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>GIVEEFFECT_IN
				
					
				<br/>
			</td>
			<td>When entering the zone it gives an effect
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>GIVEEFFECT_OUT
				
					
				<br/>
			</td>
			<td>When leaving the zone it gives an effect
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>MELT
				
					
				<br/>
			</td>
			<td>Disable ice/snow melting
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>TELEPORT
				
					
				<br/>
			</td>
			<td>When set the owner can teleport to the zone with the gui
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>WATER_FLOW
				
					
				<br/>
			</td>
			<td>Disable water flow
				
					
				<br/>
			</td>
		</tr>
		<tr>
			<td>LAVA_FLOW
				
					
				<br/>
			</td>
			<td>Disable lava flow
				
					
				<br/>
			</td>
		</tr>
	</tbody>
</table>
<p>
	<br/>
</p>
<h3>
	<a id="configuration">Configuration</a>
</h3>
<pre>
	<code>
on-create:
  protection: true
  monster: false
  animal: false
  welcome: false
  farewell: false
  heal: false
  hurt: false
  pvp: false
  creeper: false
  tnt: false
  explosion: false
  fire: false
  restriction: false
  jail: false
  lightning: false
  deathdrop: false
  safedeath: false
  drop: false
  interact: true
  enderman: false
  god: false
  gamemode: false
  fly: false
  takeitem_in: false
  takeitem_out: false
  giveitem_in: false
  giveitem_out: false
  takeeffect_in: false
  takeeffect_out: false
  giveeffect_in: false
  giveeffect_out: false
  melt: false
  teleport: false
  water_flow: false
  lava_flow: false
locale: hu
listeners:
  healthListener: true
tools:
  check: WOOD_SWORD
  define: WOOD_SPADE
particles:
  enabled: true
  particle: FIREWORKS_SPARK
autoexpand:
  enabled: true
messages:
  title:
    enabled: true
    fadeIn: 2
    stay: 20
    fadeOut: 2
healing:
  time: 3
  amount: 1
hurting:
  time: 7
  amount: 2
restriction:
  size:
    (-1, -1, -1): izone.zone.max-size.unlimited
    (50, 256, 50): izone.zone.max-size.1
  zone:
    '-1': izone.zone.max-zone.unlimited
    '3': izone.zone.max-zone.1
</code>
</pre>
<br/>
<p align="center">
	<strong>This plugin utilizes BtoBastian&rsquo;s plugin metrics system, which means that the following information is collected and sent to bstats.org</strong>
</p>
<br/>
<p align="center">
  Made by <a href="https://github.com/TryHardDood/" rel="nofollow" target="_blank">TryHardDood</a><span>&nbsp;</span>with ❤️
</p>
