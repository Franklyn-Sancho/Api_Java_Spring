import jdk.internal.net.http.ResponseContent;

@RestController
@RequestMapping("/api-travels/travels")

public class TravelController {

    private static final Logger logger = Logger.getLogger(TravelController.class);

    @Autowired
    private TravelService travelService;

    @GetMapping
    public responseEntity<List<Travel>> find() {
        if(travelService.find().isEmpty()) {
            return responseEntity.notFound().build();
        }
        logger.info(travelService.find());
        return responseEntity.ok(travelService.find());
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delete() {
        try {
            travelService.delete();
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            logger.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    @ResponseBody

    public ResponseEntity<Travel> create(@ResquestBody JSONobject travel) {
        try {
            if(travelService.isJSONValid(travel.toString())) {
                Travel travelCreated = travelService.create(travel);
                var uri = servletUriComponentBuilder.fromCurrentRequest().path(travelCreated.getOrderNumber()).build.toUri();

                if(travelService.isStartDateGreaterThanEndDate(travelCreated)) {
                    logger.error("The date is grater than end date.");
                    return ResponseEntity.status(httpStatus.UNPROCESSABLE_ENTITY).body(null);
                } else {
                    travelService.add(travelCreated);
                    return responseEntity.created(uri).body(null);
                }
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch(Exception e) {
            logger.error("JSON fields are not parsable. " + e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @PutMapping(path = "/{id}", produces = { "application/json" })

    public ResponseEntity<Travel> update(@PathVariable("id") long id, @ResquestBody JSONObject travel) {
        try {
            if (travelService.isJSONValid(travel.toString())) {
                Travel travelToUpdate = travelService.findById(id);
                if(travelToUpdate == null) {
                    logger.error("Travel not found");
                    return ResponseEntity.notFound().build();
                } else {
                    Travel travelToUpdate = travelService.update(travelToUpdate, travel);
                    return ResponseEntity.ok(travelToUpdate);
                }
            } else {
                return ResponseEntity.badResquest().body(null);
            }
        }catch(Exception e) {
			logger.error("JSON fields are not parsable." + e);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }


}