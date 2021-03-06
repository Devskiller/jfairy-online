package io.codearte.jFairyOnline.rest;

import java.net.URI;
import java.util.List;

import io.codearte.jFairyOnline.dto.DataPackDTO;
import io.codearte.jFairyOnline.model.DataPack;
import io.codearte.jFairyOnline.services.DataService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author Olga Maciaszek-Sharma
 * @since 8/29/17
 */
@RestController("restDataController")
@RequestMapping("rest/data")
public class DataController {

	private final DataService dataService;

	public DataController(DataService dataService) {
		this.dataService = dataService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Void> addDataPack(@RequestBody DataPackDTO dto) {
		DataPack dataPack = dataService.savePackWithoutContributor(dto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{dataPackId}")
				.buildAndExpand(dataPack.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping
	public ResponseEntity<List<DataPack>> getDataPacks() {
		List<DataPack> dataPacks = dataService.getDataPacks();
		return ResponseEntity.ok(dataPacks);
	}

	@GetMapping("/{dataPackId}")
	public ResponseEntity<DataPack> getDataPack(@PathVariable String dataPackId) {
		DataPack dataPack = dataService.getDataPack(dataPackId);
		return ResponseEntity.ok(dataPack);
	}

	@DeleteMapping("/{dataPackId}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<Void> deleteDataItems(@PathVariable String dataPackId,
	                                            @RequestParam(value = "dataItem", required = false) Long[] dataItemIds) {
		dataService.deleteDataItems(dataPackId, dataItemIds);
		return ResponseEntity.accepted().build();
	}

	@PutMapping("/{dataPackId}/process")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<DataPack> process(@PathVariable String dataPackId) {
		DataPack dataPack = dataService.process(dataPackId);
		return ResponseEntity.ok(dataPack);
	}
}
